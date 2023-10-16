package com.coffeeshopauthentication.service;

import com.coffeeshopauthentication.dto.request.LoginRequest;
import com.coffeeshopauthentication.dto.request.RegisterRequest;
import com.coffeeshopauthentication.dto.response.UserLoginResponse;

public interface UserService {

    UserLoginResponse login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);
    
}
