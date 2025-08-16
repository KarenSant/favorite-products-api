package com.favoriteproducts.api.domain.port;

import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepositoryPort {
    Optional<Produto> findByClientAndProductId(Cliente cliente, Long productId);

    Produto save(Produto p);

    void delete(Produto p);

    List<Produto> findByClientId(Long clientId);
}