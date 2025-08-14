package com.favoriteproducts.api.domain.port;

import com.favoriteproducts.api.domain.model.ProdutoFavorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoFavoritoRepository extends JpaRepository<ProdutoFavorito, Long> {
    Optional<ProdutoFavorito> findByIdAndClienteId(Long id, Long clienteId);

    List<ProdutoFavorito> findByClienteId(Long clienteId);
}