package com.api_gateway.security;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

        public static final List<String> openApiEndPoints = List.of(
                        "/studentservice/api",
                        "/authservice/api",
                        "/masterservice/api",
                        "eureka");

        public Predicate<ServerHttpRequest> isSecured = request -> openApiEndPoints
                        .stream()
                        .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
