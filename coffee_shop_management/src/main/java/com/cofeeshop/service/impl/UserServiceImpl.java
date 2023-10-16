package com.cofeeshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cofeeshop.client.KeycloakClient;
import com.cofeeshop.dto.request.LoginRequest;
import com.cofeeshop.dto.request.RegisterRequest;
import com.cofeeshop.dto.response.UserLoginResponse;
import com.cofeeshop.service.UserService;

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
