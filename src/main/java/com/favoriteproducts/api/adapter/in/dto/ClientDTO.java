package com.favoriteproducts.api.adapter.in.dto;

import com.favoriteproducts.api.domain.model.ProdutoDetalhes;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ClientDTO {
    @Getter
    @Setter
    public static class Create {
        @NotBlank
        private String name;
        @Email
        @NotBlank
        private String email;
    }

    @Getter
    @Setter
    public static class Update {
        @NotBlank
        private String name;
        @Email
        @NotBlank
        private String email;
    }

    @Getter
    @Setter
    @Builder
    public static class View {
        private Long id;
        private String name;
        private String email;
        private List<ProdutoDetalhes> favoritos;
    }
}

