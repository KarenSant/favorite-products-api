package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.adapter.in.dto.ProdutoDTO;
import com.favoriteproducts.api.adapter.in.dto.ProdutoFavoritoDTO;
import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.ProdutoFavorito;
import com.favoriteproducts.api.domain.port.out.ClienteRepository;
import com.favoriteproducts.api.domain.port.out.ProdutoFavoritoRepository;
import com.favoriteproducts.api.exception.ResourceNotFoundException;
import com.favoriteproducts.api.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ProdutoFavoritoService {

    private static final Logger logger = LogUtil.getLogger(ProdutoFavoritoService.class);

    private final ProdutoFavoritoRepository produtoFavoritoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoApiService produtoApiService;

    public ProdutoFavoritoService(ProdutoFavoritoRepository produtoFavoritoRepository, ClienteRepository clienteRepository,
                                  ProdutoApiService produtoApiService) {
        this.produtoFavoritoRepository = produtoFavoritoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoApiService = produtoApiService;
    }


    public ProdutoDTO adicionarProdutoFavorito(Long clienteId, Long produtoId) {
        logger.info("Validando produto com ID: {} via API externa...", produtoId);

        ProdutoDTO produtoDTO = produtoApiService.buscarProdutoPorId(produtoId);
        if (produtoDTO == null) {
            logger.warn("Produto com ID: {} não encontrado na API externa.", produtoId);
            throw new ResourceNotFoundException("Produto com ID: " + produtoId + " não encontrado na API externa.");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID: " + clienteId + " não encontrado"));

        ProdutoFavorito produtoFavorito = new ProdutoFavorito();
        produtoFavorito.setTitle(produtoDTO.getTitle());
        produtoFavorito.setImage(produtoDTO.getImage());
        produtoFavorito.setPrice(produtoDTO.getPrice());
        produtoFavorito.setReview(produtoDTO.getReview());
        produtoFavorito.setCliente(cliente);

        ProdutoFavorito savedProduto = produtoFavoritoRepository.save(produtoFavorito);
        logger.info("Produto favorito adicionado com sucesso: {}", savedProduto);

        return toDTO(savedProduto);
    }

    public void removerProdutoFavorito(Long clienteId, Long id) {
        logger.info("Removendo produto favorito com ID: {} para o cliente com ID: {}", id, clienteId);

        ProdutoFavorito produtoFavorito = produtoFavoritoRepository.findByIdAndClienteId(id, clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto favorito com ID: " + id + " não encontrado para o cliente com ID: " + clienteId));

        produtoFavoritoRepository.delete(produtoFavorito);
        logger.info("Produto favorito com ID: {} removido com sucesso para o cliente com ID: {}", id, clienteId);
    }

    private ProdutoDTO toDTO(ProdutoFavorito produtoFavorito) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produtoFavorito.getId());
        dto.setTitle(produtoFavorito.getTitle());
        dto.setImage(produtoFavorito.getImage());
        dto.setPrice(produtoFavorito.getPrice());
        dto.setReview(produtoFavorito.getReview());
        return dto;
    }

}