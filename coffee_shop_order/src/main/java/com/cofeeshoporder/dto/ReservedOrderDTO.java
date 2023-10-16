package com.cofeeshoporder.dto;

import org.springframework.beans.BeanUtils;

import com.cofeeshoporder.entity.ReservedOrder;
import com.cofeeshoporder.entity.ReservedOrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class ReservedOrderDTO {
    
    private Long id;

    private Long reservedOrderQueueId;

    private Long dishId;

    private String customerName;

    private Long reservedTime;

    private ReservedOrderStatus status;

    public ReservedOrderDTO(ReservedOrder reservedOrder) {
        BeanUtils.copyProperties(reservedOrder, this);
    }

}
