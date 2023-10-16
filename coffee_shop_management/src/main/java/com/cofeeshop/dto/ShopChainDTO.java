package com.cofeeshop.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.cofeeshop.entity.ShopChain;
import com.cofeeshop.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Data
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class ShopChainDTO {

    private Long id;

    private String shopChainName;

    private String shopChainOwner;

    List<ShopDTO> shops;
    
    private Status status;

    public ShopChainDTO(ShopChain shopChain) {
        BeanUtils.copyProperties(shopChain, this);
    }
}
