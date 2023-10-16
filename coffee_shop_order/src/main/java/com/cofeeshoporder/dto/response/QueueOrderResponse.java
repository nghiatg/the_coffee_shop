package com.cofeeshoporder.dto.response;

import java.util.List;
import java.util.Map;

import com.cofeeshoporder.dto.ReservedOrderDTO;
import com.cofeeshoporder.entity.ReservedOrderStatus;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class QueueOrderResponse {

    Map<ReservedOrderStatus, Integer> orderStatus;
    
    List<ReservedOrderDTO> inProgressOrderList;
    
    List<ReservedOrderDTO> newOrderList;

}
