package com.favoriteproducts.api.application.mapper;

import com.favoriteproducts.api.adapter.in.dto.ProdutoDTO;
import com.favoriteproducts.api.domain.model.ProdutoFavorito;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public static ProdutoDTO toDTO(ProdutoFavorito produtoFavorito) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produtoFavorito.getId());
        dto.setTitle(produtoFavorito.getTitle());
        dto.setImage(produtoFavorito.getImage());
        dto.setPrice(produtoFavorito.getPrice());
        dto.setReview(produtoFavorito.getReview());
        return dto;
    }

    public static ProdutoFavorito toEntity(ProdutoDTO produtoDTO) {
        ProdutoFavorito produtoFavorito = new ProdutoFavorito();
        produtoFavorito.setTitle(produtoDTO.getTitle());
        produtoFavorito.setImage(produtoDTO.getImage());
        produtoFavorito.setPrice(produtoDTO.getPrice());
        produtoFavorito.setReview(produtoDTO.getReview());
        return produtoFavorito;
    }

    public static ProdutoDTO filtrarCamposProduto(ProdutoDTO produtoDTO) {
        ProdutoDTO produtoFiltrado = new ProdutoDTO();
        produtoFiltrado.setId(produtoDTO.getId());
        produtoFiltrado.setTitle(produtoDTO.getTitle());
        produtoFiltrado.setImage(produtoDTO.getImage());
        produtoFiltrado.setPrice(produtoDTO.getPrice());
        produtoFiltrado.setReview(produtoDTO.getReview());
        return produtoFiltrado;
    }

}