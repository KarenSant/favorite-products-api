package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.ProdutoDTO;
import com.favoriteproducts.api.application.service.ProdutoApiService;
import com.favoriteproducts.api.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/{clienteId}/produtos-favoritos")
public class ProdutoController {

    private static final Logger logger = LogUtil.getLogger(ProdutoController.class);

    private final ProdutoApiService produtoApiService;

    public ProdutoController(ProdutoApiService produtoApiService) {
        this.produtoApiService = produtoApiService;
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> adicionarProdutoFavorito(@PathVariable Long clienteId, @RequestBody ProdutoDTO produtoDTO) {
        logger.info("Iniciando processo para adicionar produto favorito. Cliente ID: {}", clienteId);

        try {
            ProdutoDTO produtoAdicionado = produtoApiService.adicionarProdutoFavorito(clienteId, produtoDTO.getId());
            logger.info("Produto favorito adicionado com sucesso. Cliente ID: {}, Produto Favorito ID: {}", clienteId, produtoAdicionado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoAdicionado);
        } catch (Exception e) {
            logger.error("Erro ao adicionar produto favorito. Cliente ID: {}, Erro: {}", clienteId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarProdutosFavoritos(@PathVariable Long clienteId) {
        logger.info("Iniciando listagem de produtos favoritos. Cliente ID: {}", clienteId);

        try {
            List<ProdutoDTO> produtos = produtoApiService.listarProdutoFavorito(clienteId);
            logger.info("Listagem concluída com sucesso. Cliente ID: {}, Total de produtos encontrados: {}", clienteId, produtos.size());
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            logger.error("Erro ao listar produtos favoritos. Cliente ID: {}, Erro: {}", clienteId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProdutoFavorito(@PathVariable Long clienteId, @PathVariable Long id) {
        logger.info("Iniciando remoção de produto favorito. Cliente ID: {}, Produto Favorito ID: {}", clienteId, id);

        try {
            produtoApiService.removerProdutoFavorito(clienteId, id);
            logger.info("Produto favorito removido com sucesso. Cliente ID: {}, Produto Favorito ID: {}", clienteId, id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erro ao remover produto favorito. Cliente ID: {}, Produto Favorito ID: {}, Erro: {}", clienteId, id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}