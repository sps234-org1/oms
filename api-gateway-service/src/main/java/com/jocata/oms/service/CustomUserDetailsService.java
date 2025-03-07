package com.jocata.oms.service;

import com.jocata.oms.response.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

//@Service
//public class CustomUserDetailsService implements ReactiveUserDetailsService {
//
//    @Autowired
//    private WebClient webClient;
//
//    @Autowired
//    public CustomUserDetailsService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.build();
//    }
//
//    public Mono<UserBean> getUserByEmail(String url) {
//        return webClient.get()
//                .uri(url)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, response ->
//                        Mono.error(new RuntimeException("Error fetching user data: " + response.statusCode()))
//                )
//                .bodyToMono(UserBean.class)
//                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found!")));
//    }
//
//    @Override
//    public Mono<UserDetails> findByUsername(String email) {
//
//        try {
//            String url = "http://localhost:8081/user-mgmt-service/api/v1/user/getByEmail?email=" + email;
//            return getUserByEmail(url)
//                    .map(user -> User.builder()
//                            .username(user.getEmail())
//                            .password("{noop}" + user.getPasswordHash())
//                            .roles(user.getRoles().get(0).getRoleName())
//                            .build())
//                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with email: " + email)));
//
//        } catch (Exception e) {
//            return Mono.error(new UsernameNotFoundException("User not found with email: " + email));
//        }
//    }
//}
//
//
//
