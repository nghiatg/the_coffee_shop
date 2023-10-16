package com.coffeeshopauthentication.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeeshopauthentication.client.KeycloakClient;
import com.coffeeshopauthentication.dto.request.LoginRequest;
import com.coffeeshopauthentication.dto.request.RegisterRequest;
import com.coffeeshopauthentication.dto.response.UserLoginResponse;
import com.coffeeshopauthentication.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private KeycloakClient keycloakClient;

    @Override
    public UserLoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        return keycloakClient.login(username, password);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        keycloakClient.createUser(registerRequest);
    }

    
    
}
