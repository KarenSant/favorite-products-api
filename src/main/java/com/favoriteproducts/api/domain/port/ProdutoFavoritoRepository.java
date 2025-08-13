package com.favoriteproducts.api.domain.port;

import com.favoriteproducts.api.domain.model.ProdutoFavorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoFavoritoRepository extends JpaRepository<ProdutoFavorito, Long> {
    List<ProdutoFavorito> findByIdAndClienteId(Long produtoId, Long clienteId);
}
