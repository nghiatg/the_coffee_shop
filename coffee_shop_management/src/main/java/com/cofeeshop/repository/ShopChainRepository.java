package com.cofeeshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cofeeshop.entity.ShopChain;

@Repository
public interface ShopChainRepository extends JpaRepository<ShopChain, Long> {

    List<ShopChain> findByShopChainOwner(String owner);
    
}
