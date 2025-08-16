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

@Component
//@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ApplicationContext context;

    public TokenFilter(JwtService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

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

//    private final JwtService jwtService;
//    private final @Lazy UserDetailsService uds;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
//            throws ServletException, IOException {
//        String auth = req.getHeader("Authorization");
//        if (auth != null && auth.startsWith("Bearer ")) {
//            String token = auth.substring(7);
//            try {
//                String user = jwtService.validateAndGetSubject(token);
//                UserDetails ud = uds.loadUserByUsername(user);
//                var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
//                        ud, null, ud.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            } catch (Exception ignored) {
//            }
//        }
//        chain.doFilter(req, res);
//    }
//}
