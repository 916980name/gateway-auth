package com.example.apigateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.apigateway.bean.LoginRequest;
import com.example.apigateway.config.RefreshRouteLocator;
import com.example.apigateway.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final RefreshRouteLocator refreshRouteLocator;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    } 

    // @GetMapping("/refresh")
    // public String refresh() {
    //     refreshRouteLocator.buildRoutes();
    //     return "success";
    // }
}
    

