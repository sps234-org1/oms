package com.jocata.oms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDetailsConfig {

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {

        UserDetails admin = User.builder()
                .username("admin1")
                .password(passwordEncoder().encode("admin1"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("user1"))
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
