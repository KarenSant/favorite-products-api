package com.favoriteproducts.api.adapter.out;

import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.port.ClientRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepositoryPort {
    private final ClienteRepository repo;

    public Cliente save(Cliente c) {
        return repo.save(c);
    }

    public Optional<Cliente> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<Cliente> findByEmail(String email) {
        return repo.findByEmailIgnoreCase(email);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Page<Cliente> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }
}