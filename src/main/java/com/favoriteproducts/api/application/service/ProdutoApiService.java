package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.adapter.in.dto.ProdutoDTO;
import com.favoriteproducts.api.application.mapper.ProdutoMapper;
import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.ProdutoFavorito;
import com.favoriteproducts.api.domain.port.ClienteRepository;
import com.favoriteproducts.api.domain.port.ProdutoFavoritoRepository;
import com.favoriteproducts.api.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoApiService {

    private static final Logger logger = LogUtil.getLogger(ProdutoApiService.class);
    private final ProdutoFavoritoRepository produtoFavoritoRepository;
    private final ClienteRepository clienteRepository;
    private final ApiExternaService apiExternaService;

    public ProdutoApiService(ProdutoFavoritoRepository produtoFavoritoRepository,
                             ClienteRepository clienteRepository,
                             ApiExternaService apiExternaService) {
        this.produtoFavoritoRepository = produtoFavoritoRepository;
        this.clienteRepository = clienteRepository;
        this.apiExternaService = apiExternaService;
    }

    @Transactional
    public ProdutoDTO adicionarProdutoFavorito(Long clienteId, Long produtoId) {
        logger.info("Iniciando processo para adicionar produto favorito. Cliente ID: {}, Produto ID: {}", clienteId, produtoId);

        Cliente cliente = buscarClientePorId(clienteId);
        validarProdutoDuplicado(clienteId, produtoId);

        ProdutoDTO produtoDTO = validarProdutoNaApiExterna(produtoId);
        ProdutoFavorito produtoFavorito = salvarProdutoFavorito(cliente, produtoDTO);

        logger.info("Produto adicionado com sucesso à lista de favoritos. Cliente ID: {}, Produto ID: {}", clienteId, produtoId);
        return ProdutoMapper.toDTO(produtoFavorito);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarProdutoFavorito(Long clienteId) {
        logger.info("Buscando produtos favoritos do cliente. Cliente ID: {}", clienteId);

        Cliente cliente = buscarClientePorId(clienteId);
        List<ProdutoFavorito> produtosFavoritos = produtoFavoritoRepository.findByClienteId(cliente.getId());

        return produtosFavoritos.stream()
                .map(ProdutoMapper::toDTO)
                .toList();
    }

    @Transactional
    public void removerProdutoFavorito(Long clienteId, Long produtoId) {
        logger.info("Iniciando remoção de produto favorito. Cliente ID: {}, Produto ID: {}", clienteId, produtoId);

        Cliente cliente = buscarClientePorId(clienteId);
        ProdutoFavorito produtoFavorito = produtoFavoritoRepository.findByIdAndClienteId(produtoId, cliente.getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto favorito não encontrado"));

        produtoFavoritoRepository.delete(produtoFavorito);
        logger.info("Produto favorito removido com sucesso. Cliente ID: {}, Produto ID: {}", clienteId, produtoId);
    }

    private Cliente buscarClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    private void validarProdutoDuplicado(Long clienteId, Long produtoId) {
        boolean produtoJaFavorito = produtoFavoritoRepository.findByIdAndClienteId(produtoId, clienteId).isPresent();
        if (produtoJaFavorito) {
            throw new IllegalArgumentException("Produto já está na lista de favoritos");
        }
    }

    private ProdutoDTO validarProdutoNaApiExterna(Long produtoId) {
        ProdutoDTO produtoDTO = apiExternaService.buscarProdutoPorId(produtoId);
        if (produtoDTO == null || isProdutoInvalido(produtoDTO)) {
            throw new IllegalArgumentException("Produto inválido ou não encontrado na API externa");
        }
        return produtoDTO;
    }

    private boolean isProdutoInvalido(ProdutoDTO produtoDTO) {
        return produtoDTO.getId() == null || produtoDTO.getTitle() == null ||
                produtoDTO.getImage() == null || produtoDTO.getPrice() == null;
    }

    private ProdutoFavorito salvarProdutoFavorito(Cliente cliente, ProdutoDTO produtoDTO) {
        ProdutoFavorito produtoFavorito = ProdutoMapper.toEntity(produtoDTO);
        produtoFavorito.setCliente(cliente);
        return produtoFavoritoRepository.save(produtoFavorito);
    }
}