package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.adapter.in.dto.ProdutoDTO;
import com.favoriteproducts.api.domain.port.ProdutoFavoritoRepository;
import com.favoriteproducts.api.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiExternaService {

    private static final Logger logger = LogUtil.getLogger(ApiExternaService.class);
    private final RestTemplate restTemplate;
    private final ClienteService clienteService;
    private final ProdutoFavoritoRepository produtoFavoritoRepository;
    private static final String API_URL = "https://fakestoreapi.com/products/";

    public ApiExternaService(RestTemplate restTemplate,
                             ClienteService clienteService,
                             ProdutoFavoritoRepository produtoFavoritoRepository) {
        this.restTemplate = restTemplate;
        this.clienteService = clienteService;
        this.produtoFavoritoRepository = produtoFavoritoRepository;
    }

    public ProdutoDTO buscarProdutoPorId(Long produtoId) {
        String url = API_URL + produtoId;
        logger.info("Buscando produto na API externa. Produto ID: {}", produtoId);

        return restTemplate.getForObject(url, ProdutoDTO.class);
    }

}