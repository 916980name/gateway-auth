package com.example.apigateway.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.apigateway.bean.BizUser;
import com.example.apigateway.bean.LoginRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserWebRequest {
    private final WebClient userWebClient;

    public Mono<BizUser> login(LoginRequest request) {
        return userWebClient.post()
                .uri("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(BizUser.class);
    }
}
