package com.cofeeshoporder.dto.request;

import java.util.List;

import com.cofeeshoporder.dto.UserType;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    
    private String password;
    
    private List<UserType> userTypes;
}
