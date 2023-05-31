package com.example.apigateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebClientConfig {
    @Value("${feign.user-service.url}")
    private String userUrl;

    @Bean(name = "userWebClient")
    public WebClient generateUserWebClient() {
        log.info("userUrl: {}", userUrl);
        WebClient webClient = WebClient.builder()
                .baseUrl(userUrl)
                .build();
        return webClient;
    }

}
