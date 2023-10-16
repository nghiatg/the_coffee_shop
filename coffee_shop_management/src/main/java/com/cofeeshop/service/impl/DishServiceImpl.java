package com.cofeeshop.service.impl;

import static com.cofeeshop.config.Constants.DISH_NOT_FOUND;
import static com.cofeeshop.config.Constants.SHOP_NOT_FOUND;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cofeeshop.dto.DishDTO;
import com.cofeeshop.entity.Dish;
import com.cofeeshop.entity.Shop;
import com.cofeeshop.exception.ClientException;
import com.cofeeshop.repository.DishRepository;
import com.cofeeshop.repository.ShopRepository;
import com.cofeeshop.service.DishService;
import com.cofeeshop.service.ShopService;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopService shopService;

    @Override
    public void updateDish(Long dishId, DishDTO dishDTO) {
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ClientException(DISH_NOT_FOUND));
        Shop shop = shopRepository.findById(dish.getShopId()).orElseThrow(() -> new ClientException(SHOP_NOT_FOUND));
        shopService.checkCurrentUserCanManageShop(shop);

        Optional.ofNullable(dishDTO.getName()).ifPresent(dish::setName);
        Optional.ofNullable(dishDTO.getPrice()).ifPresent(dish::setPrice);

        dishRepository.save(dish);
    }

    @Override
    public DishDTO getDish(Long dishId) {
        Dish dish = dishRepository.findById(dishId).orElseThrow(() -> new ClientException(DISH_NOT_FOUND));
        return new DishDTO(dish);
    }

    

    
    
}
