package com.jocata.oms.service;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Component
public class CustomAuthenticationConverter implements ServerAuthenticationConverter {


    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        List<String> authHeaders = exchange.getRequest().getHeaders().get("Authorization");
        if (authHeaders == null || authHeaders.isEmpty()) {
            return Mono.empty();
        }
        String authHeader = authHeaders.get(0);
        if (!authHeader.startsWith("Basic")) {
            return Mono.empty();
        }

        String base64Credentials = authHeader.substring(6);
        byte[] decodedBytes;
        try {
            decodedBytes = Base64.getDecoder().decode(base64Credentials);
        } catch (IllegalArgumentException e) {
            return Mono.empty();
        }

        String decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] credentials = decodedCredentials.split(":", 2);
        if (credentials.length != 2) {
            return Mono.empty();
        }

        String username = credentials[0];
        String password = credentials[1];

        return Mono.just(new UsernamePasswordAuthenticationToken(username, password));
    }



}
