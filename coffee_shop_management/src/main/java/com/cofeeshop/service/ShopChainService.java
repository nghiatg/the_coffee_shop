package com.cofeeshop.service;

import java.util.List;

import com.cofeeshop.dto.ShopChainDTO;
import com.cofeeshop.dto.ShopDTO;

public interface ShopChainService {

    List<ShopChainDTO> retrieveShopChainList();
    
    Long createShopChain(ShopChainDTO shopChainDTO);
    
    void deleteShopChain(Long shopChainId);
    
    void updateShopChain(Long shopChainId, ShopChainDTO shopChainDTO);
    
    Long addShopToShopChain(Long shopChainId, ShopDTO shopDTO);
    
    void deleteShop(Long shopChainId, Long shopId);
}
