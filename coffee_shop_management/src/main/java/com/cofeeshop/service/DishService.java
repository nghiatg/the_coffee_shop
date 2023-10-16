package com.cofeeshop.service;

import com.cofeeshop.dto.DishDTO;

public interface DishService {

    void updateDish(Long dishId, DishDTO dishDTO);

    DishDTO getDish(Long dishId);
    
}
