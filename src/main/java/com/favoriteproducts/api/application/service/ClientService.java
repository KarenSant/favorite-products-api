package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.port.ClientRepositoryPort;
import com.favoriteproducts.api.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepositoryPort clientRepo;

    public Cliente create(Cliente c) {
        try {
            log.info("Iniciando criação de cliente com email={}", c.getEmail());

            clientRepo.findByEmail(c.getEmail()).ifPresent(x -> {
                throw new DomainException("O email já está em uso: " + c.getEmail());
            });
            Cliente novoCliente = clientRepo.save(c);
            log.info("Cliente criado com sucesso. ID={}, email={}", novoCliente.getId(), novoCliente.getEmail());

            return novoCliente;
        } catch (DomainException e) {
            log.error("Erro ao criar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar cliente. Email={}", c.getEmail(), e);
            throw new DomainException("Erro inesperado ao criar cliente.");
        }
    }

    public Cliente update(Long id, Cliente payload) {
        try {
            log.info("Iniciando atualização do cliente com ID={}", id);

            Cliente existente = get(id);
            if (!existente.getEmail().equalsIgnoreCase(payload.getEmail())) {
                clientRepo.findByEmail(payload.getEmail()).ifPresent(x -> {
                    throw new DomainException("O email já está em uso: " + payload.getEmail());
                });
            }
            existente.setName(payload.getName());
            existente.setEmail(payload.getEmail());
            Cliente atualizado = clientRepo.save(existente);

            log.info("Cliente atualizado com sucesso. ID={}, email={}", atualizado.getId(), atualizado.getEmail());
            return atualizado;
        } catch (DomainException e) {
            log.error("Erro ao atualizar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar cliente. ID={}", id, e);
            throw new DomainException("Erro inesperado ao atualizar cliente.");
        }
    }

    public void delete(Long id) {
        try {
            log.info("Iniciando exclusão do cliente com ID={}", id);

            clientRepo.deleteById(id);
            log.info("Cliente excluído com sucesso. ID={}", id);
        } catch (Exception e) {
            log.error("Erro inesperado ao excluir cliente. ID={}", id, e);
            throw new DomainException("Erro inesperado ao excluir cliente.");
        }
    }

    public Cliente get(Long id) {
        try {
            log.info("Buscando cliente com ID={}", id);

            return clientRepo.findById(id)
                    .orElseThrow(() -> new DomainException("Cliente não encontrado com o ID: " + id));
        } catch (DomainException e) {
            log.error("Erro ao buscar cliente: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar cliente. ID={}", id, e);
            throw new DomainException("Erro inesperado ao buscar cliente.");
        }
    }

    public Page<Cliente> list(Pageable pageable) {
        try {
            log.info("Listando clientes com paginação: {}", pageable);

            Page<Cliente> clientes = clientRepo.findAll(pageable);
            log.info("Clientes listados com sucesso. Total de registros: {}", clientes.getTotalElements());
            return clientes;
        } catch (Exception e) {
            log.error("Erro inesperado ao listar clientes.", e);
            throw new DomainException("Erro inesperado ao listar clientes.");
        }
    }
}