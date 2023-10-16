package com.cofeeshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cofeeshop.entity.ReservedOrderQueue;

@Repository
public interface ReservedOrderQueueRepository extends JpaRepository<ReservedOrderQueue, Long> {

    List<ReservedOrderQueue> findByShopId(Long shopId);
    
}
