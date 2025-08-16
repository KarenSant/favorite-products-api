package com.favoriteproducts.api.application.security;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Classe de configuração para o encoder de senhas.
 */
@Configuration
@Schema(name = "PasswordConfig", description = "Configuração para o encoder de senhas utilizando BCrypt.")
public class PasswordConfig {

    /**
     * Bean responsável por fornecer o encoder de senhas.
     *
     * @return uma instância de PasswordEncoder configurada com BCrypt.
     */
    @Bean
    @Schema(description = "Bean que fornece o encoder de senhas BCrypt.")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}