package com.cofeeshoporder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cofeeshoporder.client.KeycloakClient;
import com.cofeeshoporder.dto.request.LoginRequest;
import com.cofeeshoporder.dto.request.RegisterRequest;
import com.cofeeshoporder.dto.response.UserLoginResponse;
import com.cofeeshoporder.service.UserService;

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
