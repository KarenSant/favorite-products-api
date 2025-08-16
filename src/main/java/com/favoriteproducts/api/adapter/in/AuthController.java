package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.AuthDTO;
import com.favoriteproducts.api.application.service.JwtService;
//import com.favoriteproducts.api.config.JwtTokenProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthenticationManager authManager;
        private final JwtService jwtService;
        private final UserDetailsService uds;
        private final PasswordEncoder pe;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, @Lazy UserDetailsService uds, PasswordEncoder pe) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.uds = uds;
        this.pe = pe;
    }

    @PostMapping("/signup")
        public ResponseEntity<AuthDTO.Token> signup(@Valid @RequestBody AuthDTO.Signup dto) {
            // Apenas para demo: guarda na memória
            if (uds instanceof InMemoryUserDetailsManager im && !im.userExists(dto.getUsername())) {
                im.createUser(User.withUsername(dto.getUsername()).password(pe.encode(dto.getPassword())).roles("USER").build());
            }
            String token = jwtService.generate(dto.getUsername());
            return ResponseEntity.ok(AuthDTO.Token.builder().accessToken(token).expiresIn(3600).build());
        }

        @PostMapping("/login")
        public ResponseEntity<AuthDTO.Token> login(@Valid @RequestBody AuthDTO.Login dto) {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            String token = jwtService.generate(dto.getUsername());
            return ResponseEntity.ok(AuthDTO.Token.builder().accessToken(token).expiresIn(3600).build());
        }

}