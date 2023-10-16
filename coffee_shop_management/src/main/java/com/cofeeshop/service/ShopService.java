package com.cofeeshop.service;

import java.util.List;

import com.cofeeshop.dto.ContactDTO;
import com.cofeeshop.dto.DishDTO;
import com.cofeeshop.dto.ReservedOrderQueueDTO;
import com.cofeeshop.dto.ShopDTO;
import com.cofeeshop.entity.Shop;

public interface ShopService {
    
    void deleteShop(Shop shop);
    
    ShopDTO getShopDetail(Long shopId);
    
    void updateShopInfo(Long shopId, ShopDTO shopDTO);
    
    Long addDishToShop(Long shopId, DishDTO dishDTO);

    void checkCurrentUserCanManageShop(Shop shop);
    
    void removeDishFromShop(Long shopId, Long dishId);
    
    void addContactToShop(Long shopId, ContactDTO contactDTO);
    
    void removeContactFromShop(Long shopId, Long conactId);
    
    Long addOrderQueueToShop(Long shopId, ReservedOrderQueueDTO queueDTO);
    
    void removeOrderQueueFromShop(Long shopId, Long orderQueueId);
    
    List<ShopDTO> findNearestShops(Double longitude, Double latitude, Integer numberOfShops);

}
