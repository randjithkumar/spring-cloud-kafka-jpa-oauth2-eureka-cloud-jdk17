package com.myapp.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Employee service routes
            .route("employee-service", r -> r.path("/employee/**")
                .uri("lb://employee-service/"))
            .route("department-service", r -> r.path("/department/**")
                .uri("lb://department-service/"))
            .route("organization-service", r -> r.path("/organization/**")
                .uri("lb://organization-service/"))
            .build();
    }
}
