package com.cofeeshop.dto;

import org.springframework.beans.BeanUtils;

import com.cofeeshop.entity.ReservedOrderQueue;
import com.cofeeshop.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
@AllArgsConstructor
public class ReservedOrderQueueDTO {

    private Long id;

    private Integer maxSize;

    private ShopDTO shop;
    
    private Status status;

    public ReservedOrderQueueDTO(ReservedOrderQueue reservedOrderQueue) {
        BeanUtils.copyProperties(reservedOrderQueue, this);
    }

}
