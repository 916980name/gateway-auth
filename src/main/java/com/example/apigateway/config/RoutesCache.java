package com.example.apigateway.config;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.example.apigateway.bean.Routes;
import com.example.apigateway.repo.RoutesRepo;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RoutesCache {
    private final RoutesRepo routesRepo;

    private ConcurrentHashMap<String, Routes> routeCache = new ConcurrentHashMap<>();

    @PostConstruct
    private void generateRouteCache() {
        routesRepo.findAll().flatMap(entity -> {
            routeCache.put(String.valueOf(entity.getId()), entity);
            return Mono.just(entity);
        })
        .subscribe();
    }
    
    public ConcurrentHashMap<String, Routes> getCache() {
        return routeCache;
    }
}
