package com.example.apigateway.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.apigateway.bean.Routes;

public interface RoutesRepo extends ReactiveCrudRepository<Routes, Integer> {
    
}
