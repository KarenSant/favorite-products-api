package com.favoriteproducts.api.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoDetalhes {
    private Long id;
    private String title;
    private String image;
    private Double price;
    private Double rating;
}