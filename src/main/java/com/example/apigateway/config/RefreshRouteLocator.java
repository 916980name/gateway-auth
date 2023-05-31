package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.BooleanSpec;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.apigateway.bean.Routes;
import com.example.apigateway.repo.RoutesRepo;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

/**
 * https://medium.com/bliblidotcom-techblog/spring-cloud-gateway-dynamic-routes-from-database-dc938c6665de
 */
@Component
@RequiredArgsConstructor
public class RefreshRouteLocator implements RouteLocator {
    /**
     * https://www.jianshu.com/p/490739b183af
     */
    private final RoutesRepo routesRepo;
    private final RouteLocatorBuilder builder;
    private final AuthenticationFilter authenticationFilter;

    // public void buildRoutes() {
    // clearRoutes();
    // if (routesBuilder != null) {
    // routesRepo.findAll().parallelStream().forEach(service -> {
    // String serviceId = String.valueOf(service.getId());
    // URI uri =
    // UriComponentsBuilder.fromHttpUrl(service.getRoute()).build().toUri();
    // routesBuilder.route(serviceId, r -> r.path(service.getPath()).uri(uri));
    // });
    // this.route = routesBuilder.build().getRoutes();
    // }
    // gatewayRoutesRefresher.refreshRoutes();
    // }

    @Override
    public Flux<Route> getRoutes() {
        RouteLocatorBuilder.Builder routesBuilder = builder.routes();
        return routesRepo.findAll()
                .map(apiRoute -> routesBuilder.route(String.valueOf(apiRoute.getId()),
                        predicateSpec -> setPredicateSpec(apiRoute, predicateSpec)))
                .collectList()
                .flatMapMany(builders -> routesBuilder.build().getRoutes());
    }

    private Buildable<Route> setPredicateSpec(Routes apiRoute, PredicateSpec predicateSpec) {
        BooleanSpec booleanSpec = predicateSpec.path(apiRoute.getPath());
        booleanSpec.filters(f -> f.filter(authenticationFilter));
        if (!StringUtils.isEmpty(apiRoute.getMethod())) {
            booleanSpec.and()
                    .method(apiRoute.getMethod().split(","));
        }
        return booleanSpec.uri(apiRoute.getRoute());
    }
}