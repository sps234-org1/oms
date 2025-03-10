package com.jocata.oms.config;

import com.jocata.oms.service.CustomAuthenticationConverter;
import com.jocata.oms.service.CustomReactiveAuthenticationManager;
import com.jocata.oms.service.CustomSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final CustomAuthenticationConverter customAuthenticationConverter;
    private final CustomReactiveAuthenticationManager customReactiveAuthenticationManager;
    private final CustomSecurityContextRepository customSecurityContextRepository;

    public SecurityConfig(@Lazy CustomAuthenticationConverter customAuthenticationConverter,
                          @Lazy CustomReactiveAuthenticationManager customReactiveAuthenticationManager,
                          CustomSecurityContextRepository customSecurityContextRepository) {
        this.customAuthenticationConverter = customAuthenticationConverter;
        this.customReactiveAuthenticationManager = customReactiveAuthenticationManager;
        this.customSecurityContextRepository = customSecurityContextRepository;
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authFilter = new AuthenticationWebFilter(customReactiveAuthenticationManager);
        authFilter.setServerAuthenticationConverter(customAuthenticationConverter);
        authFilter.setSecurityContextRepository(customSecurityContextRepository);
        return authFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationWebFilter authenticationWebFilter) {
        return
                http
                        .csrf(ServerHttpSecurity.CsrfSpec::disable)
                        .authorizeExchange(auth -> auth
                                .pathMatchers("/user-mgmt-service/api/v1/auth/user/**").hasAnyRole("USER", "ADMIN")
                                .pathMatchers("/user-mgmt-service/api/v1/user/**").hasAnyRole("USER", "ADMIN")
                                .pathMatchers("/user-mgmt-service/api/v1/user/**").hasAnyAuthority("USER", "ADMIN")
                                .pathMatchers("/user-mgmt-service/api/v1/admin/**").hasAuthority("ADMIN")
                                .pathMatchers("/user-mgmt-service/api/v1/public/**").permitAll()
                                .anyExchange().authenticated())
                        .addFilterAfter(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                        .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
