package com.favoriteproducts.api.adapter.in.dto.favoriteDTO;

import com.favoriteproducts.api.domain.model.ProdutoDetalhes;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FavoriteView {
    private Long id;
    private String title;
    private String image;
    private Double price;
    private Double review;
    public static FavoriteView from(ProdutoDetalhes p) {
        return FavoriteView.builder()
                .id(p.getId())
                .title(p.getTitle())
                .image(p.getImage())
                .price(p.getPrice())
                .review(p.getRating())
                .build();
    }
}
