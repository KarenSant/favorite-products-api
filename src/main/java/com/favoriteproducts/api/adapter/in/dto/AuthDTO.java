package com.favoriteproducts.api.adapter.in.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

public class AuthDTO {
    @Getter
    @Setter
    public static class Signup {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class Login {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class Token {
        private String accessToken;
        private long expiresIn;
    }
}
