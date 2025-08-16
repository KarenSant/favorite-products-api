package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.favoriteDTO.FavoriteView;
import com.favoriteproducts.api.application.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clients/{clientId}/favorites")
@RequiredArgsConstructor
@Tag(name = "Produtos Favoritos", description = "Endpoints para gerenciar os produtos favoritos dos clientes")
public class ProdutoController {
    private final ProdutoService service;

    @PostMapping("/{productId}")
    @Operation(
        summary = "Adicionar produto aos favoritos",
        description = "Adiciona um produto à lista de favoritos de um cliente.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Produto adicionado aos favoritos com sucesso."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cliente ou produto não encontrado."
            )
        }
    )
    public ResponseEntity<Void> add(@PathVariable Long clientId, @PathVariable Long productId) {
        service.addFavorite(clientId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    @Operation(
        summary = "Remover produto dos favoritos",
        description = "Remove um produto da lista de favoritos de um cliente.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Produto removido dos favoritos com sucesso."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cliente ou produto não encontrado."
            )
        }
    )
    public ResponseEntity<Void> remove(@PathVariable Long clientId, @PathVariable Long productId) {
        service.removeFavorite(clientId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
        summary = "Listar produtos favoritos",
        description = "Retorna a lista de produtos favoritos de um cliente.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de produtos favoritos retornada com sucesso.",
                content = @Content(schema = @Schema(implementation = FavoriteView.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cliente não encontrado."
            )
        }
    )
    public List<FavoriteView> list(@PathVariable Long clientId) {
        return service.listFavorites(clientId).stream()
                .map(FavoriteView::from)
                .toList();
    }
}