package com.favoriteproducts.api.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração de segurança para a aplicação.
 * Configura a autenticação, autorização e gerenciamento de sessão.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final @Lazy UserDetailsService uds;
    private final TokenFilter tokenFilter;

    /**
     * Configura o filtro de segurança da aplicação.
     *
     * @param http o objeto HttpSecurity para configurar as regras de segurança.
     * @return o filtro de segurança configurado.
     * @throws Exception caso ocorra algum erro na configuração.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/health",
                                "/v3/api-docs/",
                                "/swagger-ui/",
                                "/swagger-ui.html",
                                "/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                ).addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configura o gerenciador de autenticação da aplicação.
     *
     * @param uds o serviço de detalhes do usuário.
     * @param pe  o codificador de senhas.
     * @return o gerenciador de autenticação configurado.
     */
    @Bean
    public AuthenticationManager authenticationManager(@Lazy UserDetailsService uds, PasswordEncoder pe) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(pe);
        return new ProviderManager(provider);
    }
}