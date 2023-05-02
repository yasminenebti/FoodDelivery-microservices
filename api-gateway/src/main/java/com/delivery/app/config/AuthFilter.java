package com.delivery.app.config;

import com.delivery.clients.users.UserRequest;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
          if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
              throw new RuntimeException("There is no auth header");
          }
            String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if(token!=null && token.startsWith("Bearer ")) {
                token=token.substring(7);
            }
            return webClientBuilder.build()
                    .get()
                    .uri("lb://USERS/api/v1/auth/validateToken/"+token)
                    .retrieve().bodyToMono(UserRequest.class)
                    .map(userRequest -> {
                       exchange.getRequest()
                               .mutate()
                               .header("x-auth-user-id", String.valueOf(userRequest.id()))
                               .header("x-auth-user-name", String.valueOf(userRequest.name()));
                     return exchange;
                    }).flatMap(chain::filter);
        });
    }

    public static class Config{}
}
