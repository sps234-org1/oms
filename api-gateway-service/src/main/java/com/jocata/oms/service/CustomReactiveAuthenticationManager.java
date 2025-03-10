package com.jocata.oms.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder pwdEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = pwdEncoder;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        return userDetailsService.findByUsername(username).flatMap(userDetails -> {
            System.out.printf("inp pwd : %s , encoded pwd : %s\n", password, userDetails.getPassword());
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return Mono.just((Authentication) new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities()));
            } else {
                return Mono.error(new BadCredentialsException("Invalid Credentials"));
            }
        }).switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }


}
