package com.cofeeshop.dto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cofeeshop.entity.Shop;
import com.cofeeshop.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Data
@Jacksonized
@AllArgsConstructor
public class ShopDTO {
    
    private Long id;

    private String shopOperator;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String openTime;

    private String closeTime;

    private ShopChainDTO shopChain;

    private List<DishDTO> dishs;

    private List<ContactDTO> contacts;
    
    private Status status;

    private List<ReservedOrderQueueDTO> reservedOrderQueues;

    public ShopDTO(Shop shop) {
        BeanUtils.copyProperties(shop, this);
    }
    
}
