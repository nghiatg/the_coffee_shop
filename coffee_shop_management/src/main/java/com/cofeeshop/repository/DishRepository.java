package com.cofeeshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cofeeshop.entity.Dish;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByShopId(Long shopId);
    
}
