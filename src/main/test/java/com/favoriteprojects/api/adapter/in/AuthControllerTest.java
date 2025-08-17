package main.test.java.com.favoriteprojects.api.adapter.in;

import com.favoriteproducts.api.adapter.dto.AuthDTO;
import com.favoriteproducts.api.config.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("Deve autenticar e retornar token JWT")
    void deveAutenticarERetornarToken() throws Exception {
        String username = "admin";
        String password = "admin123";
        String token = "jwt-token-fake";

        Mockito.doNothing().when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.when(jwtService.generate(username)).thenReturn(token);

        String requestBody = """
            {
                "username": "admin",
                "password": "admin123"
            }
        """;

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".accessToken").value(token))
                .andExpect(jsonPath(".expiresIn").value(3600));
    }

    @Test
    @DisplayName("Deve retornar 401 Não Autorizado para credenciais inválidas")
    void deveRetornar401ParaCredenciaisInvalidas() throws Exception {
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Credenciais inválidas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"usuario_invalido\", \"password\":\"senha_errada\"}"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Deve retornar 400 Bad Request quando username e password estiverem ausentes")
    void deveRetornarBadRequestParaCredenciaisAusentes() throws Exception {
        String payload = "{}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Username and password are required"));
    }


    @Test
    @DisplayName("Deve retornar 400 Bad Request para formato JSON inválido")
    void deveRetornarBadRequestParaFormatoJsonInvalido() throws Exception {
        String payloadInvalido = "invalid-json";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadInvalido))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Invalid JSON format"));
    }


