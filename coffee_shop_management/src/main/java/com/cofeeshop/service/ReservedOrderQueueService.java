package com.cofeeshop.service;

import java.util.List;

import com.cofeeshop.dto.ReservedOrderQueueDTO;

public interface ReservedOrderQueueService {

    ReservedOrderQueueDTO getOrderQueue(Long orderQueueId);

    List<ReservedOrderQueueDTO> listAllManagedQueue();

    void updateOrderQueue(Long orderQueueId, ReservedOrderQueueDTO orderQueueDTO);
    
}
