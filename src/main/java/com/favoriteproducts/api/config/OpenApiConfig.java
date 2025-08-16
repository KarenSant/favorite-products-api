package com.favoriteproducts.api.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info().title("Customers & Favorites API")
                .description("API RESTful com clientes e favoritos, integração FakeStore, " +
                        "JWT e arquitetura hexagonal")
                .version("1.0.0"));
    }
}
