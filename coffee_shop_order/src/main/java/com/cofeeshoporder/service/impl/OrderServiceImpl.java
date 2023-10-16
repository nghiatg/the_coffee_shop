package com.cofeeshoporder.service.impl;

import static com.cofeeshoporder.config.Constants.DISH_ALREADY_DELETED;
import static com.cofeeshoporder.config.Constants.NOT_ENOUGH_PERMISSION;
import static com.cofeeshoporder.config.Constants.ORDER_NOT_FOUND;
import static com.cofeeshoporder.config.Constants.ORDER_QUEUE_ALREADY_DELETED;
import static com.cofeeshoporder.config.Constants.ORDER_QUEUE_IS_FULL;
import static com.cofeeshoporder.config.Constants.ORDER_STATUS_INVALID;
import static com.cofeeshoporder.dto.DTOStatus.AVAILABLE;
import static com.cofeeshoporder.entity.ReservedOrderStatus.CANCELLED;
import static com.cofeeshoporder.entity.ReservedOrderStatus.DONE;
import static com.cofeeshoporder.entity.ReservedOrderStatus.NEW;
import static com.cofeeshoporder.entity.ReservedOrderStatus.PROCESSING;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cofeeshoporder.client.CoffeeShopMngClient;
import com.cofeeshoporder.dto.DishDTO;
import com.cofeeshoporder.dto.ReservedOrderDTO;
import com.cofeeshoporder.dto.ReservedOrderQueueDTO;
import com.cofeeshoporder.dto.request.OrderHistoryRequest;
import com.cofeeshoporder.dto.response.OrderDetailResponse;
import com.cofeeshoporder.dto.response.OrderDetailResponse.DishInfo;
import com.cofeeshoporder.dto.response.OrderDetailResponse.QueueInfo;
import com.cofeeshoporder.dto.response.QueueOrderResponse;
import com.cofeeshoporder.entity.ReservedOrder;
import com.cofeeshoporder.exception.ClientException;
import com.cofeeshoporder.repository.ReservedOrderRepository;
import com.cofeeshoporder.service.OrderService;
import com.cofeeshoporder.util.UserUtils;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ReservedOrderRepository orderRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private CoffeeShopMngClient coffeeShopMngClient;

    @Override
    public Long placeOrder(ReservedOrderDTO reservedOrderDTO) {
        DishDTO dish = coffeeShopMngClient.getDishInfo(reservedOrderDTO.getDishId());
        Optional.ofNullable(dish.getStatus()).filter(AVAILABLE::equals).orElseThrow(() -> new ClientException(DISH_ALREADY_DELETED));
        ReservedOrderQueueDTO orderQueue = 
            coffeeShopMngClient.getOrderQueueInfo(reservedOrderDTO.getReservedOrderQueueId());
        Optional.ofNullable(orderQueue.getStatus()).filter(AVAILABLE::equals).orElseThrow(() -> new ClientException(ORDER_QUEUE_ALREADY_DELETED));

        int waitingOrderInQueue = orderRepository.findByReservedOrderQueueIdAndStatus(reservedOrderDTO.getReservedOrderQueueId(), NEW).size();
        if(waitingOrderInQueue >= orderQueue.getMaxSize()) {
            throw new ClientException(ORDER_QUEUE_IS_FULL);
        }

        ReservedOrder createdOrder = new ReservedOrder();
        BeanUtils.copyProperties(reservedOrderDTO, createdOrder);
        createdOrder.setCustomer(userUtils.retrieveCurrentUser());
        createdOrder.setReservedTime(Instant.now().toEpochMilli());
        createdOrder.setStatus(NEW);
        
        return orderRepository.save(createdOrder).getId();
    }

    @Override
    public void cancelOrder(Long orderId) {
        ReservedOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ClientException(ORDER_NOT_FOUND));
        checkCurrentUserCanManageOrder(order);
        Optional.of(order.getStatus()).filter(NEW::equals).orElseThrow(() -> new ClientException(ORDER_STATUS_INVALID));
        order.setStatus(CANCELLED);
        orderRepository.save(order);
    }


    private void checkCurrentUserCanManageOrder(ReservedOrder order) {
        String currentUserName = userUtils.retrieveCurrentUser();
        Optional.of(order.getCustomer())
            .filter(currentUserName::equals)
            .orElseThrow(() -> new ClientException(NOT_ENOUGH_PERMISSION));

    }

    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        ReservedOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ClientException(ORDER_NOT_FOUND));
        checkCurrentUserCanManageOrder(order);

        OrderDetailResponse orderDetailResponse = OrderDetailResponse.builder().build();
        BeanUtils.copyProperties(order, orderDetailResponse);
        DishDTO dish = coffeeShopMngClient.getDishInfo(order.getDishId());
        orderDetailResponse.setDishInfo(DishInfo.builder().id(dish.getId()).name(dish.getName()).build());
        Integer positionInQueue = 1 + orderRepository.countByReservedOrderQueueIdAndStatusAndIdLessThan(order.getReservedOrderQueueId(), NEW, orderId);
        orderDetailResponse.setOrderQueueInfo(
            QueueInfo.builder()
                        .positionInQueue(order.getStatus() == NEW ? positionInQueue : null)
                        .id(order.getReservedOrderQueueId())
                        .build()
            );
        return orderDetailResponse;
    }

    @Override
    public QueueOrderResponse getQueueOrder(Long orderQueueId) {
        coffeeShopMngClient.getOrderQueueInfo(orderQueueId);

        List<ReservedOrder> newPlacedOrders = orderRepository.findByReservedOrderQueueIdAndStatus(orderQueueId, NEW);
        List<ReservedOrder> inProgressOrders = orderRepository.findByReservedOrderQueueIdAndStatus(orderQueueId, PROCESSING);

        QueueOrderResponse queueOrderResponse = QueueOrderResponse.builder().build();
        queueOrderResponse.setOrderStatus(
            Map.of(
                NEW, newPlacedOrders.size(),
                PROCESSING, inProgressOrders.size()
            )
        );

        queueOrderResponse.setNewOrderList(newPlacedOrders.stream().map(ReservedOrderDTO::new).collect(Collectors.toList()));
        queueOrderResponse.setInProgressOrderList(inProgressOrders.stream().map(ReservedOrderDTO::new).collect(Collectors.toList()));

        return queueOrderResponse;
    }

    @Override
    public void processOrder(Long orderId) {
        ReservedOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ClientException(ORDER_NOT_FOUND));
        Optional.of(order.getStatus()).filter(NEW::equals).orElseThrow(() -> new ClientException(ORDER_STATUS_INVALID));

        Long orderQueueId = order.getReservedOrderQueueId();
        coffeeShopMngClient.getOrderQueueInfo(orderQueueId);

        order.setStatus(PROCESSING);
        orderRepository.save(order);
    }

    @Override
    public void finishOrder(Long orderId) {
        ReservedOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ClientException(ORDER_NOT_FOUND));
        Optional.of(order.getStatus()).filter(PROCESSING::equals).orElseThrow(() -> new ClientException(ORDER_STATUS_INVALID));

        Long orderQueueId = order.getReservedOrderQueueId();
        coffeeShopMngClient.getOrderQueueInfo(orderQueueId);

        order.setStatus(DONE);
        orderRepository.save(order);
    }

    @Override
    public List<ReservedOrderDTO> getOrderHistoryOfUser(OrderHistoryRequest orderHistoryRequest) {
        String customerName = orderHistoryRequest.getCustomerName();
        
        ReservedOrderQueueDTO[] orderQueues = coffeeShopMngClient.getAllManagedQueues();
        List<ReservedOrderDTO> orderDTOs = 
                Arrays.stream(orderQueues).map(ReservedOrderQueueDTO::getId)
                    .map(orderQueueId -> orderRepository.findByReservedOrderQueueIdAndStatus(orderQueueId, DONE))
                    .flatMap(List::stream)
                    .filter(order -> customerName.equals(order.getCustomer()))
                    .map(ReservedOrderDTO::new)
                    .sorted((a, b) -> a.getId().compareTo(b.getId()))
                    .collect(Collectors.toList());
                    
        return orderDTOs;
    }

    

}
