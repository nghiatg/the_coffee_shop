package com.cofeeshop.dto.response;

import lombok.Data;

@Data
public class KeycloakClientLoginResponse {

    private String accessToken;

    private Integer expiresIn;
    
}
