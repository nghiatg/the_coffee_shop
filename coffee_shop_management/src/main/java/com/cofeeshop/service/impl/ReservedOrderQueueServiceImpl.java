package com.cofeeshop.service.impl;

import static com.cofeeshop.config.Constants.ORDER_QUEUE_NOT_FOUND;
import static com.cofeeshop.config.Constants.SHOP_NOT_FOUND;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cofeeshop.dto.ReservedOrderQueueDTO;
import com.cofeeshop.entity.ReservedOrderQueue;
import com.cofeeshop.entity.Shop;
import com.cofeeshop.entity.ShopChain;
import com.cofeeshop.exception.ClientException;
import com.cofeeshop.repository.ReservedOrderQueueRepository;
import com.cofeeshop.repository.ShopChainRepository;
import com.cofeeshop.repository.ShopRepository;
import com.cofeeshop.service.ReservedOrderQueueService;
import com.cofeeshop.service.ShopService;
import com.cofeeshop.util.UserUtils;

@Service
public class ReservedOrderQueueServiceImpl implements ReservedOrderQueueService {

    @Autowired
    private ReservedOrderQueueRepository reservedOrderQueueRepository;

    @Autowired
    private ShopChainRepository shopChainRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserUtils userUtils;

    @Override
    public void updateOrderQueue(Long orderQueueId, ReservedOrderQueueDTO orderQueueDTO) {
        ReservedOrderQueue orderQueue = 
            reservedOrderQueueRepository.findById(orderQueueId).orElseThrow(() -> new ClientException(ORDER_QUEUE_NOT_FOUND));
        Shop shop = shopRepository.findById(orderQueue.getShopId()).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        shopService.checkCurrentUserCanManageShop(shop);

        Optional.ofNullable(orderQueueDTO.getMaxSize()).ifPresent(orderQueue::setMaxSize);
        reservedOrderQueueRepository.save(orderQueue);
    }

    @Override
    public ReservedOrderQueueDTO getOrderQueue(Long orderQueueId) {
        ReservedOrderQueue orderQueue = 
            reservedOrderQueueRepository.findById(orderQueueId).orElseThrow(() -> new ClientException(ORDER_QUEUE_NOT_FOUND));
        return new ReservedOrderQueueDTO(orderQueue);
    }

    @Override
    public List<ReservedOrderQueueDTO> listAllManagedQueue() {
        String currentUser = userUtils.retrieveCurrentUser();
        List<ShopChain> shopChains = shopChainRepository.findByShopChainOwner(currentUser);
        List<Shop> shops = shopChains.stream()
                                    .map(ShopChain::getId)
                                    .map(shopRepository::findByShopChainId)
                                    .flatMap(List::stream)
                                    .collect(Collectors.toList());
        return shops.stream().map(Shop::getId)
                    .map(reservedOrderQueueRepository::findByShopId)
                    .flatMap(List::stream)
                    .map(ReservedOrderQueueDTO::new)
                    .collect(Collectors.toList());
    }    

    

}
