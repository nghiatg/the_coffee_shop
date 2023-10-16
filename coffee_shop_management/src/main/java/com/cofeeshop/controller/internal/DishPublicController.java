package com.cofeeshop.controller.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.service.DishService;

@RequestMapping("/dish-internal")
@RestController
public class DishPublicController {

    @Autowired
    private DishService dishService;

    @GetMapping("/{dishId}")
    public GenericApiResponse getDishInfo(@PathVariable Long dishId) {
        return GenericApiResponse.builder().data(dishService.getDish(dishId)).build();
    }
}