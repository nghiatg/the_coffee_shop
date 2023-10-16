package com.cofeeshoporder.dto;

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
    
    private DTOStatus status;
}
