package com.cofeeshop.dto.response;

import lombok.Data;

@Data
public class UserLoginResponse {

    private String accessToken;

    private Integer expiresIn;
    
    private Integer refreshExpiresIn;
    
    private String refreshToken;
}
