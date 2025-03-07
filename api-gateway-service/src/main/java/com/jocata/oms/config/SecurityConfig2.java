package com.jocata.oms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//
//@Configuration
//@EnableWebFluxSecurity
//public class SecurityConfig2 {
//
//    /*
//    @Bean
//    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
//        http.csrf(ServerHttpSecurity.CsrfSpec::disable).
//                authorizeExchange(exchanges -> exchanges.anyExchange().authenticated()).httpBasic();
//        return http.build();
//    }
//
//     */
//
//
//    @Bean
//    public ServerAuthenticationEntryPoint customAuthenticationEntryPoint() {
//
//        return (exchange, ex) -> {
//            ServerWebExchange response = exchange;
//            response.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            response.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
//
//            String body = """
//                        {
//                            "status": 401,
//                            "error": "Unauthorized",
//                            "message": "Access Denied! Please provide valid credentials."
//                        }
//                    """;
//
//            return response.getResponse().writeWith(
//                    Mono.just(response.getResponse()
//                            .bufferFactory()
//                            .wrap(body.getBytes()))
//            );
//        };
//    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF for APIs
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/user-mgmt-service/api/v1/admin/**").hasRole("ADMIN")
//                        .pathMatchers("/user-mgmt-service/api/v1/user/**").hasAnyRole("USER", "ADMIN" )
//                        .pathMatchers(HttpMethod.GET, "/user-mgmt-service/api/v1/**").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .httpBasic(httpBasic -> httpBasic
//                        .authenticationEntryPoint(customAuthenticationEntryPoint()));
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsRepositoryReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService) {
//        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
//    }
//
//}
//
//
