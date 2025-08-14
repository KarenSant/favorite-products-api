package com.favoriteproducts.api.application.service;

import com.favoriteproducts.api.adapter.in.dto.ClienteDTO;
import com.favoriteproducts.api.application.mapper.ClienteMapper;
import com.favoriteproducts.api.domain.model.Cliente;
import com.favoriteproducts.api.domain.port.ClienteRepository;
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
        validarEmailDuplicado(clienteDTO.getEmail());

        Cliente cliente = ClienteMapper.toEntity(clienteDTO);
        Cliente clienteSalvo = clienteRepository.save(cliente);

        logger.info("Cliente criado com sucesso: {}", clienteSalvo.getId());
        return ClienteMapper.toDTO(clienteSalvo);
    }

    public List<ClienteDTO> listarClientes() {
        logger.info("Listando todos os clientes.");
        return clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarClientePorId(Long id) {
        logger.info("Buscando cliente com ID: {}", id);
        Cliente cliente = obterClientePorId(id);

        logger.info("Cliente encontrado: {}", cliente.getId());
        return ClienteMapper.toDTO(cliente);
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteAtualizado) {
        logger.info("Atualizando cliente com ID: {}", id);
        Cliente cliente = obterClientePorId(id);

        atualizarDadosCliente(cliente, clienteAtualizado);

        Cliente clienteSalvo = clienteRepository.save(cliente);
        logger.info("Cliente atualizado com sucesso: {}", clienteSalvo.getId());
        return ClienteMapper.toDTO(clienteSalvo);
    }

    public void removerCliente(Long id) {
        logger.info("Removendo cliente com ID: {}", id);
        Cliente cliente = obterClientePorId(id);

        clienteRepository.delete(cliente);
        logger.info("Cliente removido com sucesso: {}", id);
    }

    private void validarEmailDuplicado(String email) {
        if (clienteRepository.findByEmail(email).isPresent()) {
            logger.error("E-mail já cadastrado: {}", email);
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
    }

    private Cliente obterClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }

    private void atualizarDadosCliente(Cliente cliente, ClienteDTO clienteAtualizado) {
        if (clienteAtualizado.getUsername() != null) {
            cliente.setUsername(clienteAtualizado.getUsername());
        }
        if (clienteAtualizado.getEmail() != null) {
            cliente.setEmail(clienteAtualizado.getEmail());
        }
        if (clienteAtualizado.getPassword() != null) {
            cliente.setPassword(clienteAtualizado.getPassword());
        }
    }
}