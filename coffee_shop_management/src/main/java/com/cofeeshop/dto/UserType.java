package com.cofeeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    SHOP_CHAIN_OWNER("shop_chain_owner", "ROLE_SHOP_CHAIN_OWNER"), 
    SHOP_OPERATOR("shop_operator", "ROLE_SHOP_OPERATOR"), 
    CUSTOMER("customer", "ROLE_CUSTOMER");

    private String keyCloakGroupName;

    private String authorityName;
    
}
