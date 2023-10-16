package com.cofeeshoporder.dto.response;

import com.cofeeshoporder.entity.ReservedOrderStatus;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class OrderDetailResponse {
    
    private Long id;

    private QueueInfo orderQueueInfo;

    private DishInfo dishInfo;

    private String customerName;

    private Long reservedTime;

    private ReservedOrderStatus status;

    @Builder
    @Data
    @Jacksonized
    public static class QueueInfo {
    
        private Long id;
        
        private Integer positionInQueue;

    }

    @Builder
    @Data
    @Jacksonized
    public static class DishInfo {

        private Long id;
        
        private String name;

    }

}
