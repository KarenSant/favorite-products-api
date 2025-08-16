package com.favoriteproducts.api.adapter.out;


import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.Produto;
import com.favoriteproducts.api.domain.port.ProdutoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProdutoRepositoryAdapter implements ProdutoRepositoryPort {
    private final ProdutoRepository repo;

    public Optional<Produto> findByClientAndProductId(Cliente cliente, Long productId) {
        return repo.findByClienteAndId(cliente, productId);
    }

    public Produto save(Produto p) {
        return repo.save(p);
    }

    public void delete(Produto p) {
        repo.delete(p);
    }

    public List<Produto> findByClientId(Long clientId) {
        return repo.findByClienteId(clientId);
    }

}