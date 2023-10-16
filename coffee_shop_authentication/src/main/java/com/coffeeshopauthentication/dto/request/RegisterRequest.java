package com.coffeeshopauthentication.dto.request;

import java.util.List;

import com.coffeeshopauthentication.dto.UserType;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    
    private String password;
    
    private List<UserType> userTypes;
}
