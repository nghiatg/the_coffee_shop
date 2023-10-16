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

import com.cofeeshop.dto.ContactDTO;
import com.cofeeshop.dto.DishDTO;
import com.cofeeshop.dto.ReservedOrderQueueDTO;
import com.cofeeshop.dto.ShopDTO;
import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.dto.response.GenericCreateApiResponse;
import com.cofeeshop.service.ShopService;

@RequestMapping("/shop")
@RestController
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/{shopId}")
    public GenericApiResponse getShopDetail(@PathVariable Long shopId) {
        return GenericApiResponse.builder().data(shopService.getShopDetail(shopId)).build();
    }

    @PutMapping("/{shopId}")
    public GenericApiResponse updateShopInfo(@PathVariable Long shopId, @RequestBody ShopDTO shopDTO) {
        shopService.updateShopInfo(shopId, shopDTO);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @PostMapping("/{shopId}/dish")
    public GenericApiResponse addDishToShop(@PathVariable Long shopId, @RequestBody DishDTO dishDTO) {
        Long dishId = shopService.addDishToShop(shopId, dishDTO);
        return GenericApiResponse.builder().data(GenericCreateApiResponse.builder().id(dishId).build()).build();
    }

    @DeleteMapping("/{shopId}/dish/{dishId}")
    public GenericApiResponse removeDishFromShop(@PathVariable Long shopId, @PathVariable Long dishId) {
        shopService.removeDishFromShop(shopId, dishId);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @PostMapping("/{shopId}/contact")
    public GenericApiResponse addContactToShop(@PathVariable Long shopId, @RequestBody ContactDTO contactDTO) {
        shopService.addContactToShop(shopId, contactDTO);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @DeleteMapping("/{shopId}/contact/{contactId}")
    public GenericApiResponse removeContactFromShop(@PathVariable Long shopId, @PathVariable Long contactId) {
        shopService.removeContactFromShop(shopId, contactId);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

    @PostMapping("/{shopId}/order-queue")
    public GenericApiResponse addOrderQueueToShop(@PathVariable Long shopId, @RequestBody ReservedOrderQueueDTO queueDTO) {
        Long queueId = shopService.addOrderQueueToShop(shopId, queueDTO);
        return GenericApiResponse.builder().data(GenericCreateApiResponse.builder().id(queueId).build()).build();
    }

    @DeleteMapping("/{shopId}/order-queue/{orderQueueId}")
    public GenericApiResponse removeOrderQueueFromShop(@PathVariable Long shopId, @PathVariable Long orderQueueId) {
        shopService.removeOrderQueueFromShop(shopId, orderQueueId);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }
    
}