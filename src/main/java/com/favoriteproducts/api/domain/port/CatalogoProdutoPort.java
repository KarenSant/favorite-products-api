package com.favoriteproducts.api.domain.port;

import com.favoriteproducts.api.domain.model.ProdutoDetalhes;

import java.util.Optional;

public interface CatalogoProdutoPort {
    Optional<ProdutoDetalhes> getProductById(Long productId);
}

