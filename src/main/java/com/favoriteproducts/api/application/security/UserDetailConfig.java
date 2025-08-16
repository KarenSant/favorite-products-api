package com.favoriteproducts.api.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Classe de configuração para o gerenciamento de usuários em memória.
 * Define um serviço de detalhes do usuário com credenciais codificadas.
 */
@Configuration
@RequiredArgsConstructor
public class UserDetailConfig {

    private final PasswordEncoder passwordEncoder;

    /**
     * Configura o serviço de detalhes do usuário com um usuário em memória.
     *
     * @return uma instância de UserDetailsService configurada com usuários em memória.
     */
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