package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.favoriteDTO.FavoriteView;
import com.favoriteproducts.api.application.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clients/{clientId}/favorites")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService service;

    @PostMapping("/{productId}")
    public ResponseEntity<Void> add(@PathVariable Long clientId, @PathVariable Long productId) {
        service.addFavorite(clientId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> remove(@PathVariable Long clientId, @PathVariable Long productId) {
        service.removeFavorite(clientId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<FavoriteView> list(@PathVariable Long clientId) {
        return service.listFavorites(clientId).stream()
                .map(FavoriteView::from)
                .toList();
    }
}
