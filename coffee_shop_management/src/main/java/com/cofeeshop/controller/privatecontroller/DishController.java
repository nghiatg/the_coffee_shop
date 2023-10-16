package com.cofeeshop.controller.privatecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cofeeshop.dto.DishDTO;
import com.cofeeshop.dto.response.GenericApiResponse;
import com.cofeeshop.service.DishService;

@RequestMapping("/dish")
@RestController
public class DishController {

    @Autowired
    private DishService dishService;

    @PutMapping("/{dishId}")
    public GenericApiResponse updateDishInfo(@PathVariable Long dishId, @RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishId, dishDTO);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }
    
}