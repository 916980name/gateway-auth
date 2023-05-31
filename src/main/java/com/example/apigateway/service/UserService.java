package com.example.apigateway.service;

import java.security.KeyPair;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.example.apigateway.bean.BizUser;
import com.example.apigateway.bean.LoginRequest;
import com.example.apigateway.util.JsonUtils;
import com.example.apigateway.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    // private final UserWebRequest client;
    private final KeyPair jwtKeyPair;

    public String login(LoginRequest request) {
        BizUser user = BizUser.builder()
                .username("test")
                .privileges(Arrays.asList("P_HOME"))
                .build();
        String str = JsonUtils.silentMarshal(user);
        return JwtUtil.generateToken(jwtKeyPair, str);

        /*
         * Mono<BizUser> response = client.login(request);
         * BizUser user = response.block();
         * return user;
         */
        /*
         * if (response.statusCode().equals(HttpStatus.OK)) {
         * return response.bodyToMono(String.class);
         * } else if (response.statusCode().is4xxClientError()) {
         * return Mono.just("Error response");
         * } else {
         * return response.createException()
         * .flatMap(Mono::error);
         * }
         */
    }

}
