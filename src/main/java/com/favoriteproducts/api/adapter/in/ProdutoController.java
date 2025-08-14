package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.ProdutoDTO;
import com.favoriteproducts.api.application.service.ProdutoApiService;
import com.favoriteproducts.api.application.service.ProdutoFavoritoService;
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

    private final ProdutoFavoritoService produtoFavoritoService;
    private final ProdutoApiService produtoApiService;

    public ProdutoController(ProdutoFavoritoService produtoFavoritoService, ProdutoApiService produtoApiService) {
        this.produtoFavoritoService = produtoFavoritoService;
        this.produtoApiService = produtoApiService;
    }

    @PostMapping("/adicionar-da-api/{produtoId}")
    public ResponseEntity<ProdutoDTO> adicionarProdutoFavoritoDaApi(@PathVariable Long clienteId, @PathVariable Long produtoId) {
        logger.info("Iniciando processo para adicionar produto favorito. Cliente ID: {}, Produto ID: {}", clienteId, produtoId);

        try {
            ProdutoDTO produtoAdicionado = produtoFavoritoService.adicionarProdutoFavorito(clienteId, produtoId);
            logger.info("Produto favorito adicionado com sucesso. Cliente ID: {}, Produto: {}", clienteId, produtoAdicionado);
            return ResponseEntity.status(HttpStatus.CREATED).body(produtoAdicionado);
        } catch (Exception e) {
            logger.error("Erro ao adicionar produto favorito. Cliente ID: {}, Produto ID: {}, Erro: {}", clienteId, produtoId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodosProdutos() {
        logger.info("Iniciando listagem de todos os produtos da API externa.");

        try {
            List<ProdutoDTO> produtos = produtoApiService.listarTodosProdutos();
            logger.info("Listagem concluída com sucesso. Total de produtos encontrados: {}", produtos.size());
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            logger.error("Erro ao listar produtos da API externa. Erro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProdutoFavorito(@PathVariable Long clienteId, @PathVariable Long id) {
        logger.info("Iniciando remoção de produto favorito. Cliente ID: {}, Produto Favorito ID: {}", clienteId, id);

        try {
            produtoFavoritoService.removerProdutoFavorito(clienteId, id);
            logger.info("Produto favorito removido com sucesso. Cliente ID: {}, Produto Favorito ID: {}", clienteId, id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erro ao remover produto favorito. Cliente ID: {}, Produto Favorito ID: {}, Erro: {}", clienteId, id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}