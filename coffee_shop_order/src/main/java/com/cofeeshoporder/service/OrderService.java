package com.cofeeshoporder.service;


import java.util.List;

import com.cofeeshoporder.dto.ReservedOrderDTO;
import com.cofeeshoporder.dto.request.OrderHistoryRequest;
import com.cofeeshoporder.dto.response.OrderDetailResponse;
import com.cofeeshoporder.dto.response.QueueOrderResponse;

public interface OrderService {
    
    Long placeOrder(ReservedOrderDTO reservedOrderDTO);
    
    void cancelOrder(Long orderId);
    
    OrderDetailResponse getOrderDetail(Long orderId);
    
    QueueOrderResponse getQueueOrder(Long orderQueueId);
    
    void processOrder(Long orderId);
    
    void finishOrder(Long orderId);
    
    List<ReservedOrderDTO> getOrderHistoryOfUser(OrderHistoryRequest orderHistoryRequest);

}
