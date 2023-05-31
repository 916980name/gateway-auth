package com.example.apigateway.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.apigateway.bean.BizUser;
import com.example.apigateway.bean.Routes;
import com.example.apigateway.util.JsonUtils;
import com.example.apigateway.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;// custom route validator
    private final RoutesCache routeCache;
    private final KeyPair jwtKeyPair;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);

            try {
                if (JwtUtil.verifyTokenExpired(jwtKeyPair, token))
                    return this.onError(exchange, "Authorization header is invalid(expired)", HttpStatus.UNAUTHORIZED);
            } catch (Exception e) {
                log.warn("Authorization invalid token: " + token, e);
                return this.onError(exchange, "Authorization header is invalid(Completely Invalid)",
                        HttpStatus.UNAUTHORIZED);
            }

            // this.populateRequestWithHeaders(exchange, token);
            if (!authPrivilege(exchange, token)) {
                return this.onError(exchange, "Authorization Privilege is invalid", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    /* PRIVATE */

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(err.getBytes(StandardCharsets.UTF_8))));
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) throws Exception {
        Claims claims = JwtUtil.getAllClaimsFromToken(jwtKeyPair, token);

        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }

    private boolean authPrivilege(ServerWebExchange exchange, String token) {
        Route route = (Route) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route == null) {
            return false;
        }
        // check the privilege of the user whether matches the privilege defined in the
        // route table
        Routes routes = routeCache.getCache().get(route.getId());
        Set<String> routePrivileges = new HashSet<>(Arrays.asList(routes.getPrivilege().split(",")));
        if (routePrivileges.isEmpty()) {
            return true;
        }

        try {
            Claims claims = JwtUtil.getAllClaimsFromToken(jwtKeyPair, token);
            BizUser user = JsonUtils.silentUnmarshal(claims.getSubject(), BizUser.class);
            Set<String> userPrivileges = new HashSet<>(user.getPrivileges());
            return routePrivileges.containsAll(userPrivileges);
        } catch (Exception e) {
            log.error("Error in authPrivilege", e);
            return false;
        }

    }

}