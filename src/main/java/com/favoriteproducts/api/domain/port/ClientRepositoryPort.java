package com.favoriteproducts.api.domain.port;

import com.favoriteproducts.api.domain.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientRepositoryPort {//extends JpaRepository<Client, Long> {

    Cliente save(Cliente c);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByEmail(String email);

    void deleteById(Long id);

    Page<Cliente> findAll(Pageable pageable);
}