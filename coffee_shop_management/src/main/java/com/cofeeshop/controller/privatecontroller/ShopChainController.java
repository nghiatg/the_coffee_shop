package com.cofeeshop.controller.privatecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshop.dto.ShopChainDTO;
import com.cofeeshop.dto.ShopDTO;
import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.dto.response.GenericCreateApiResponse;
import com.cofeeshop.service.ShopChainService;

@RequestMapping("/shop-chain")
@RestController
public class ShopChainController {

    @Autowired
    private ShopChainService shopChainService;
        
    @GetMapping
    public GenericApiResponse getShopChainList() {
        return GenericApiResponse.builder().data(shopChainService.retrieveShopChainList()).build();
    }
        
    @PostMapping()
    public GenericApiResponse createShopChain(@RequestBody ShopChainDTO shopChainDTO) {
        Long newShopChainId = shopChainService.createShopChain(shopChainDTO);
        return GenericApiResponse.builder().data(GenericCreateApiResponse.builder().id(newShopChainId).build()).build();
    }
        
    @DeleteMapping("/{id}")
    public GenericApiResponse deleteShopChain(@PathVariable Long id) {
        shopChainService.deleteShopChain(id);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }
        
    @PutMapping("/{id}")
    public GenericApiResponse updateShopChain(@PathVariable Long id, @RequestBody ShopChainDTO shopChainDTO) {
        shopChainService.updateShopChain(id, shopChainDTO);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }
        
    @PostMapping("/{id}/shop")
    public GenericApiResponse addShopToShopChain(@PathVariable Long id, @RequestBody ShopDTO shopDTO) {
        Long newShopId = shopChainService.addShopToShopChain(id, shopDTO);
        return GenericApiResponse.builder().data(GenericCreateApiResponse.builder().id(newShopId).build()).build();
    }
        
    @DeleteMapping("/{shopChainId}/shop/{shopId}")
    public GenericApiResponse removeShopFromShopChain(@PathVariable Long shopChainId, @PathVariable Long shopId) {
        shopChainService.deleteShop(shopChainId, shopId);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }
    
}