package com.favoriteproducts.api.adapter.in.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoDTO {
    private Long id;
    private String title;
    private String image;
    private Double price;
    private String review;
}