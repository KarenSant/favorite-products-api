package main.test.java.com.favoriteprojects.api.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.favoriteprojects.api.adapter.in.ClientController;
import com.favoriteprojects.api.application.port.in.ClientService;
import com.favoriteprojects.api.application.port.out.CatalogoProdutoPort;
import com.favoriteprojects.api.application.port.out.ProdutoRepositoryPort;
import com.favoriteprojects.api.domain.ClientDTO;
import com.favoriteprojects.api.domain.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService service;

    @MockBean
    private ProdutoRepositoryPort favoriteRepo;

    @MockBean
    private CatalogoProdutoPort catalogoProdutoPort;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve criar cliente com sucesso e retornar 200")
    void deveCriarClienteComSucesso() throws Exception {
        ClientDTO.Create dto = new ClientDTO.Create();
        dto.setName("João Silva");
        dto.setEmail("joao.silva@email.com");

        Cliente clienteCriado = Cliente.builder()
                .id(1L)
                .name(dto.getName())
                .email(dto.getEmail())
                .build();

        Mockito.when(service.create(Mockito.any(Cliente.class))).thenReturn(clienteCriado);

        mockMvc.perform(post("/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
            .andExpect(jsonPath(".id").value(clienteCriado.getId()))
            .andExpect(jsonPath(".name").value(clienteCriado.getName()))
            .andExpect(jsonPath("$.email").value(clienteCriado.getEmail()));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request para dados inválidos")
    void deveRetornarBadRequestParaDadosInvalidos() throws Exception {
        ClientDTO.Create dto = new ClientDTO.Create(); 

        mockMvc.perform(post("/cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }
}