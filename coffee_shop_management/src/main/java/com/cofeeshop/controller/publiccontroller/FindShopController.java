package com.cofeeshop.controller.publiccontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.service.ShopService;


@RequestMapping("/public")
@RestController
public class FindShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/nearest-shops")
    public GenericApiResponse findNearestShop(@RequestParam Double longitude, @RequestParam Double latitude, @RequestParam Integer numberOfShops) {
        return GenericApiResponse.builder().data(shopService.findNearestShops(longitude, latitude, numberOfShops)).build();
    }
    
}
