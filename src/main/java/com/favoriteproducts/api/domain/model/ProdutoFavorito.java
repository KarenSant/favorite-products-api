package com.favoriteproducts.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ProdutoFavorito {

    @Id
    private Long id;

    private String titulo;
    private String imagem;
    private String categoria;
    private String descricao;
    private Double preco;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Override
    public String toString() {
        return "FavoriteProduct{" +
                "id=" + id +
                "titulo='" + titulo + '\'' +
                ", imagem='" + imagem + '\'' +
                ", categoria='" + categoria + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", cliente=" + (cliente != null ? cliente.getId() : "null") +
                '}';
    }

}
