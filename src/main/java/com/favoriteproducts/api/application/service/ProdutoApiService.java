package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.adapter.in.dto.ProdutoDTO;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProdutoApiService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProdutoApiService.class);
    private final RestTemplate restTemplate;

    private static final String BASE_URL = "https://fakestoreapi.com/products";

    public ProdutoApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProdutoDTO> listarTodosProdutos() {
        logger.info("Buscando todos os produtos da API pública...");

        ProdutoDTO[] produtos = restTemplate.getForObject(BASE_URL, ProdutoDTO[].class);
        logger.info("Produtos encontrados: {}", produtos != null ? produtos.length : 0);

        return produtos != null ? Arrays.asList(produtos) : List.of();
    }

    public ProdutoDTO buscarProdutoPorId(Long id) {
        logger.info("Buscando produto com ID: {} na API pública...", id);

        String url = BASE_URL + "/" + id;

        return restTemplate.getForObject(url, ProdutoDTO.class);
    }
}