package com.favoriteproducts.api.adapter.in;

import com.favoriteproducts.api.adapter.in.dto.ClientDTO;
import com.favoriteproducts.api.application.service.ClientService;
import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.model.ProdutoDetalhes;
import com.favoriteproducts.api.domain.port.CatalogoProdutoPort;
import com.favoriteproducts.api.domain.port.ProdutoRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;
    private final ProdutoRepositoryPort favoriteRepo;
    private final CatalogoProdutoPort catalogoProdutoPort;

    @PostMapping
    public ResponseEntity<ClientDTO.View> create(@Validated @RequestBody ClientDTO.Create dto) {
        Cliente c = service.create(Cliente.builder().name(dto.getName()).email(dto.getEmail()).build());

        return ResponseEntity.ok(toView(c));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO.View> get(@PathVariable Long id) {

        return ResponseEntity.ok(toView(service.get(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO.View> update(@PathVariable Long id, @Validated @RequestBody ClientDTO.Update dto) {
        Cliente updated = service.update(id, Cliente.builder().name(dto.getName()).email(dto.getEmail()).build());

        return ResponseEntity.ok(toView(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<ClientDTO.View> list(Pageable pageable) {

        return service.list(pageable).map(this::toView);
    }

    private ClientDTO.View toView(Cliente c) {
        List<ProdutoDetalhes> favoritos = favoriteRepo.findByClientId(c.getId()).stream()
                .map(fav -> catalogoProdutoPort.getProductById(fav.getId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();

        return ClientDTO.View.builder()
                .id(c.getId())
                .name(c.getName())
                .email(c.getEmail())
                .favoritos(favoritos)
                .build();
    }

}