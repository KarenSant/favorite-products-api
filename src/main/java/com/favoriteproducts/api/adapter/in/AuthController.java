package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.AuthDTO;
import com.favoriteproducts.api.application.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
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
    @Operation(
        summary = "Registrar um novo usuário",
        description = "Cria um novo usuário no sistema e retorna um token JWT.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário registrado com sucesso.",
                content = @Content(schema = @Schema(implementation = AuthDTO.Token.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos fornecidos para o registro."
            )
        }
    )
    public ResponseEntity<AuthDTO.Token> signup(@Valid @RequestBody AuthDTO.Signup dto) {
        if (uds instanceof InMemoryUserDetailsManager im && !im.userExists(dto.getUsername())) {
            im.createUser(User.withUsername(dto.getUsername()).password(pe.encode(dto.getPassword())).roles("USER").build());
        }
        String token = jwtService.generate(dto.getUsername());
        return ResponseEntity.ok(AuthDTO.Token.builder().accessToken(token).expiresIn(3600).build());
    }

    @PostMapping("/login")
    @Operation(
        summary = "Autenticar um usuário",
        description = "Autentica um usuário existente e retorna um token JWT.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Usuário autenticado com sucesso.",
                content = @Content(schema = @Schema(implementation = AuthDTO.Token.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Credenciais inválidas fornecidas."
            )
        }
    )
    public ResponseEntity<AuthDTO.Token> login(@Valid @RequestBody AuthDTO.Login dto) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        String token = jwtService.generate(dto.getUsername());
        return ResponseEntity.ok(AuthDTO.Token.builder().accessToken(token).expiresIn(3600).build());
    }
}