package com.favoriteproducts.api.adapter.out;

import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByClienteAndProductId(Cliente cliente, Long productId);

    List<Produto> findByClienteId(Long clientId);
}