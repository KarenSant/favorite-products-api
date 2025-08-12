package com.favoriteproducts.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.favorite.products.api")
public class FavoriteProductsApiApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(FavoriteProductsApiApplication.class, args);
    }
}