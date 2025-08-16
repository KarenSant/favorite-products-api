package com.favoriteproducts.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produtos", uniqueConstraints = @UniqueConstraint(columnNames = {"client_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Cliente cliente;

//    @Column(name = "product_id", nullable = false)
//    private Long productId;
}