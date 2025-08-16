package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.ClientDTO;
import com.favoriteproducts.api.application.service.ClientService;
import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.ProdutoDetalhes;
import com.favoriteproducts.api.domain.port.CatalogoProdutoPort;
import com.favoriteproducts.api.domain.port.ProdutoRepositoryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para gerenciar clientes e seus produtos favoritos")
public class ClientController {

    private final ClientService service;
    private final ProdutoRepositoryPort favoriteRepo;
    private final CatalogoProdutoPort catalogoProdutoPort;

    @PostMapping
    @Operation(
            summary = "Criar um novo cliente",
            description = "Cria um novo cliente com as informações fornecidas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente criado com sucesso.",
                            content = @Content(schema = @Schema(implementation = ClientDTO.View.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados inválidos fornecidos para criação do cliente."
                    )
            }
    )
    public ResponseEntity<ClientDTO.View> create(@Validated @RequestBody ClientDTO.Create dto) {
        Cliente c = service.create(Cliente.builder().name(dto.getName()).email(dto.getEmail()).build());
        return ResponseEntity.ok(toView(c));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obter detalhes de um cliente",
            description = "Retorna as informações de um cliente pelo ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Detalhes do cliente retornados com sucesso.",
                            content = @Content(schema = @Schema(implementation = ClientDTO.View.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado."
                    )
            }
    )
    public ResponseEntity<ClientDTO.View> get(@PathVariable Long id) {
        return ResponseEntity.ok(toView(service.get(id)));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar informações de um cliente",
            description = "Atualiza os dados de um cliente existente pelo ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cliente atualizado com sucesso.",
                            content = @Content(schema = @Schema(implementation = ClientDTO.View.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado."
                    )
            }
    )
    public ResponseEntity<ClientDTO.View> update(@PathVariable Long id, @Validated @RequestBody ClientDTO.Update dto) {
        Cliente updated = service.update(id, Cliente.builder().name(dto.getName()).email(dto.getEmail()).build());
        return ResponseEntity.ok(toView(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir um cliente",
            description = "Remove um cliente existente pelo ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Cliente excluído com sucesso."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cliente não encontrado."
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
            summary = "Listar clientes",
            description = "Retorna uma lista paginada de todos os clientes.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de clientes retornada com sucesso.",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            }
    )
    public Page<ClientDTO.View> list(Pageable pageable) {
        return service.list(pageable).map(this::toView);
    }

    private ClientDTO.View toView(Cliente c) {
        List<ProdutoDetalhes> favoritos = favoriteRepo.findByClientId(c.getId()).stream()
                .map(fav -> catalogoProdutoPort.getProductById(fav.getId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return ClientDTO.View.builder()
                .id(c.getId())
                .name(c.getName())
                .email(c.getEmail())
                .favoritos(favoritos)
                .build();
    }
}