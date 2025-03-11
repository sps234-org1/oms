package com.jocata.oms.service;

import com.jocata.oms.UserDetailsDTO;
import com.jocata.oms.response.RoleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CustomReactiveUserDetails implements ReactiveUserDetailsService {

    private final WebClient webClient;

    @Autowired
    CustomReactiveUserDetails(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8081)
                        .path("/user-mgmt-service/api/v1/auth/user/get")
                        .queryParam("userId", username)
                        .build())
                .retrieve()
                .bodyToMono(UserDetailsDTO.class)
                .map(this::convertToUserDetails)
                .doOnSuccess(user -> System.out.println("User details loaded:" + user.getPassword()))
                .doOnError(error -> System.out.println("Error fetching user details:" + error.getMessage()));
    }

    private UserDetails convertToUserDetails(UserDetailsDTO dto) {
        return User.withUsername(dto.getUserId().toString())
                .password(dto.getPasswordHash())
                .roles(dto.getRoles().stream()
                        .map(RoleBean::getRoleName)
                        .toArray(String[]::new))
                .authorities(dto.getRoles().stream()
                        .map(role -> "ROLE_" + role.getRoleName())
                        .toArray(String[]::new))
                .build();
    }

}
