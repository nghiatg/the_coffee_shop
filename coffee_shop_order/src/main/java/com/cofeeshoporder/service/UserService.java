package com.cofeeshoporder.service;

import com.cofeeshoporder.dto.request.LoginRequest;
import com.cofeeshoporder.dto.request.RegisterRequest;
import com.cofeeshoporder.dto.response.UserLoginResponse;

public interface UserService {

    UserLoginResponse login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);
    
}
