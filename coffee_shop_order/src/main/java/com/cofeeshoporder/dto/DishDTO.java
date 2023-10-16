package com.cofeeshoporder.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
@AllArgsConstructor
public class DishDTO {
    
    private Long id;

    private String name;

    private BigDecimal price;
    
    private DTOStatus status;
}