package com.favoriteproducts.api.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@Configuration
//@RequiredArgsConstructor
//public class UserDetail {
//
//    private final PasswordEncoder passwordEncoder;
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager m = new InMemoryUserDetailsManager();
//        m.createUser(User.withUsername("admin").password(passwordEncoder.encode("admin123")).roles("USER").build());
//        return m;
//    }
@Configuration
@RequiredArgsConstructor
public class UserDetailConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("USER")
                .build());
        return manager;
    }

}