package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.Produto;
import com.favoriteproducts.api.domain.model.ProdutoDetalhes;
import com.favoriteproducts.api.domain.port.CatalogoProdutoPort;
import com.favoriteproducts.api.domain.port.ClientRepositoryPort;
import com.favoriteproducts.api.domain.port.ProdutoRepositoryPort;
import com.favoriteproducts.api.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepositoryPort favoriteRepo;
    private final ClientRepositoryPort clientRepo;
    private final CatalogoProdutoPort productCatalog;

    public void addFavorite(Long clientId, Long productId) {
        try {
            Cliente cliente = clientRepo.findById(clientId)
                    .orElseThrow(() -> new DomainException("Cliente não encontrado com o ID: " + clientId));

            Optional<Produto> existing = favoriteRepo.findByClientAndProductId(cliente, productId);
            if (existing.isPresent()) {
                log.info("O produto já está nos favoritos. Cliente ID: {}, Produto ID: {}", clientId, productId);
                return;
            }

            productCatalog.getProductById(productId)
                    .orElseThrow(() -> new DomainException("Produto não encontrado com o ID: " + productId));

            favoriteRepo.save(Produto.builder().cliente(cliente).id(productId).build());
            log.info("Produto adicionado aos favoritos com sucesso. Cliente ID: {}, Produto ID: {}", clientId, productId);
        } catch (DomainException e) {
            log.error("Erro ao adicionar favorito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao adicionar favorito. Cliente ID: {}, Produto ID: {}", clientId, productId, e);
            throw new DomainException("Erro inesperado ao adicionar favorito.");
        }
    }

    public void removeFavorite(Long clientId, Long productId) {
        try {
            Cliente cliente = clientRepo.findById(clientId)
                    .orElseThrow(() -> new DomainException("Cliente não encontrado com o ID: " + clientId));

            Produto fav = favoriteRepo.findByClientAndProductId(cliente, productId)
                    .orElseThrow(() -> new DomainException("Favorito não encontrado para o Cliente ID: " + clientId + " e Produto ID: " + productId));

            favoriteRepo.delete(fav);
            log.info("Produto removido dos favoritos com sucesso. Cliente ID: {}, Produto ID: {}", clientId, productId);
        } catch (DomainException e) {
            log.error("Erro ao remover favorito: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao remover favorito. Cliente ID: {}, Produto ID: {}", clientId, productId, e);
            throw new DomainException("Erro inesperado ao remover favorito.");
        }
    }

    public List<ProdutoDetalhes> listFavorites(Long clientId) {
        try {
            clientRepo.findById(clientId)
                    .orElseThrow(() -> new DomainException("Cliente não encontrado com o ID: " + clientId));

            List<ProdutoDetalhes> favoritos = favoriteRepo.findByClientId(clientId).stream()
                    .map(f -> productCatalog.getProductById(f.getId()).orElseGet(() ->
                            ProdutoDetalhes.builder().id(f.getId()).title("Produto indisponível").build()))
                    .toList();

            log.info("Lista de favoritos recuperada com sucesso. Cliente ID: {}", clientId);
            return favoritos;
        } catch (DomainException e) {
            log.error("Erro ao listar favoritos: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao listar favoritos. Cliente ID: {}", clientId, e);
            throw new DomainException("Erro inesperado ao listar favoritos.");
        }
    }
}