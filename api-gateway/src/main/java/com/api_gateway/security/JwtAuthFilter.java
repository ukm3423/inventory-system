package com.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.handler.FilteringWebHandler;
import com.api_gateway.utils.JwtUtils;
import reactor.core.publisher.Mono;

@Service
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtils jwtUtils;

    public static class Config {
        // Configuration properties, if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Handle CORS preflight requests
            // if (request.getMethod() == HttpMethod.OPTIONS) {
            //     exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
            //     exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            //     exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            //     exchange.getResponse().setStatusCode(HttpStatus.OK);
            //     return exchange.getResponse().setComplete();
            // }

            // Proceed with authorization check for other requests
            if (validator.isSecured.test(request)) {
                // Check for Authorization header
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header missing");
                }

                String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (!authHeader.startsWith("Bearer ")) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Authorization header");
                }

                try {
                    String jwtToken = authHeader.substring(7);
                    jwtUtils.validateToken(jwtToken);
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("LoggedInUser", jwtUtils.extractUsername(jwtToken))
                            .build();
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized Access !!");
                }
            }

            return chain.filter(exchange);
        };
    }
}
