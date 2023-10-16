package com.cofeeshop.dto;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;

import com.cofeeshop.entity.Dish;
import com.cofeeshop.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
@AllArgsConstructor
public class DishDTO {
    
    private Long id;

    private String name;

    private BigDecimal price;

    private ShopDTO shop;
    
    private Status status;

    public DishDTO(Dish dish) {
        BeanUtils.copyProperties(dish, this);
    }

}
