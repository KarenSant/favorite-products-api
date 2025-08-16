package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.domain.model.ProdutoDetalhes;
import com.favoriteproducts.api.domain.port.CatalogoProdutoPort;
import com.favoriteproducts.api.exception.DomainException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatalogoProdutoService implements CatalogoProdutoPort {

    private final RestTemplate restTemplate;

    @Value("${app.external.fakestore.baseUrl}")
    private String baseUrl;

    /**
     * Busca os detalhes de um produto pelo ID no catálogo externo.
     *
     * @param productId ID do produto a ser buscado.
     * @return Um Optional contendo os detalhes do produto, se encontrado.
     * @throws DomainException Caso ocorra algum erro na busca do produto.
     */
    @Override
    @Operation(
            summary = "Buscar detalhes de um produto",
            description = "Obtém os detalhes de um produto específico a partir de um catálogo externo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Detalhes do produto retornados com sucesso.",
                            content = @Content(schema = @Schema(implementation = ProdutoDetalhes.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Produto não encontrado."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno ao buscar o produto."
                    )
            }
    )
    public Optional<ProdutoDetalhes> getProductById(Long productId) {
        try {
            String url = baseUrl + "/products/" + productId;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            Map<String, Object> body = response.getBody();
            if (body == null) return Optional.empty();

            Map<String, Object> ratingMap = (Map<String, Object>) body.get("rating");

            ProdutoDetalhes produto = ProdutoDetalhes.builder()
                    .id(((Number) body.get("id")).longValue())
                    .title((String) body.get("title"))
                    .image((String) body.get("image"))
                    .price(((Number) body.get("price")).doubleValue())
                    .rating(ratingMap != null ? ((Number) ratingMap.get("rate")).doubleValue() : null)
                    .build();

            return Optional.of(produto);
        } catch (Exception e) {
            throw new DomainException("Erro ao buscar detalhes do produto com ID: " + productId);
        }
    }
}