package com.favoriteproducts.api.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Schema(description = "Representa um cliente no sistema.")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do cliente.", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nome do cliente.", example = "João Silva")
    private String name;

    @Email(message = "E-mail inválido.")
    @Column(nullable = false, unique = true)
    @Schema(description = "E-mail do cliente.", example = "joao.silva@email.com")
    private String email;
}