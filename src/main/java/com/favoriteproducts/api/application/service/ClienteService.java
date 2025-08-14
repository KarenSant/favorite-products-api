package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.adapter.in.dto.ClienteDTO;
import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.port.out.ClienteRepository;
import com.favoriteproducts.api.exception.ResourceNotFoundException;
import com.favoriteproducts.api.util.LogUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger logger = LogUtil.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
        logger.info("Criando cliente com e-mail: {}", clienteDTO.getEmail());
        if (clienteRepository.findByEmail(clienteDTO.getEmail()).isPresent()) {
            logger.error("E-mail já cadastrado: {}", clienteDTO.getEmail());
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Cliente cliente = toEntity(clienteDTO);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        logger.info("Cliente criado com sucesso: {}", clienteSalvo.getId());
        return toDTO(clienteSalvo);
    }

    public List<ClienteDTO> listarClientes() {
        logger.info("Listando todos os clientes.");
        return clienteRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ClienteDTO buscarClientePorId(Long id) {
        logger.info("Buscando cliente com ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        logger.info("Cliente encontrado: {}", cliente.getId());
        return toDTO(cliente);
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteAtualizado) {
        logger.info("Atualizando cliente com ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        cliente.setUsername(clienteAtualizado.getUsername() != null ? clienteAtualizado.getUsername() : cliente.getUsername());
        cliente.setEmail(clienteAtualizado.getEmail() != null ? clienteAtualizado.getEmail() : cliente.getEmail());
        cliente.setPassword(clienteAtualizado.getPassword() != null ? clienteAtualizado.getPassword() : cliente.getPassword());

        Cliente clienteSalvo = clienteRepository.save(cliente);
        logger.info("Cliente atualizado com sucesso: {}", clienteSalvo.getId());
        return toDTO(clienteSalvo);
    }

    public void removerCliente(Long id) {
        logger.info("Removendo cliente com ID: {}", id);
        if (!clienteRepository.existsById(id)) {
            logger.error("Cliente não encontrado com ID: {}", id);
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
        logger.info("Cliente removido com sucesso: {}", id);
    }

    private ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setUsername(cliente.getUsername());
        dto.setEmail(cliente.getEmail());
        dto.setPassword(cliente.getPassword());
        return dto;
    }

    private Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setUsername(dto.getUsername());
        cliente.setEmail(dto.getEmail());
        cliente.setPassword(dto.getPassword());
        return cliente;
    }
}