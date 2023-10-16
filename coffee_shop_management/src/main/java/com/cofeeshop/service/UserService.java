package com.cofeeshop.service;

import com.cofeeshop.dto.request.LoginRequest;
import com.cofeeshop.dto.request.RegisterRequest;
import com.cofeeshop.dto.response.UserLoginResponse;

public interface UserService {

    UserLoginResponse login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);
    
}
