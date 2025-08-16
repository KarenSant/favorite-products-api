package com.favoriteproducts.api.application.security;

import com.favoriteproducts.api.application.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro responsável por interceptar requisições HTTP e validar o token JWT.
 * Caso o token seja válido, autentica o usuário no contexto de segurança do Spring.
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApplicationContext context;

    /**
     * Construtor da classe TokenFilter.
     *
     * @param jwtService Serviço responsável por validar e extrair informações do token JWT.
     * @param context Contexto da aplicação para obter o UserDetailsService.
     */
    public TokenFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    /**
     * Método que intercepta a requisição, valida o token JWT e autentica o usuário.
     *
     * @param req Objeto da requisição HTTP.
     * @param res Objeto da resposta HTTP.
     * @param chain Cadeia de filtros para continuar o processamento.
     * @throws ServletException Em caso de erro no processamento do filtro.
     * @throws IOException Em caso de erro de I/O.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                String user = jwtService.validateAndGetSubject(token);
                UserDetailsService uds = context.getBean(UserDetailsService.class);
                UserDetails ud = uds.loadUserByUsername(user);
                var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        ud, null, ud.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (Exception ignored) {}
        }
        chain.doFilter(req, res);
    }
}