package com.cofeeshoporder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cofeeshoporder.entity.ReservedOrder;
import com.cofeeshoporder.entity.ReservedOrderStatus;

@Repository
public interface ReservedOrderRepository extends JpaRepository<ReservedOrder, Long> {

    List<ReservedOrder> findByReservedOrderQueueIdAndStatusIn(long queueId, List<ReservedOrderStatus> statusList);

    Integer countByReservedOrderQueueIdAndStatusAndIdLessThan(Long orderQueueId, ReservedOrderStatus status, Long orderId);

    List<ReservedOrder> findByReservedOrderQueueIdAndStatus(Long orderQueueId, ReservedOrderStatus status);
    
}
