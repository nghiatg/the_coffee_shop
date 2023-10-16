package com.coffeeshopauthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffeeshopauthentication.dto.request.LoginRequest;
import com.coffeeshopauthentication.dto.request.RegisterRequest;
import com.coffeeshopauthentication.dto.response.GenericApiResponse;
import com.coffeeshopauthentication.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public GenericApiResponse login(@RequestBody LoginRequest loginRequest) {
        return GenericApiResponse.builder().data(userService.login(loginRequest)).build();
    }
    
    @PostMapping("/register")
    public GenericApiResponse register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return GenericApiResponse.buildDefaultSuccessResponse();
    }

}
