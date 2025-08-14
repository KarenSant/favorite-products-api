package com.favoriteproducts.api.application.mapper;

import com.favoriteproducts.api.adapter.in.dto.ClienteDTO;
import com.favoriteproducts.api.domain.model.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setUsername(cliente.getUsername());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setPassword(cliente.getPassword());
        return clienteDTO;
    }

    public static Cliente toEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setUsername(clienteDTO.getUsername());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setPassword(clienteDTO.getPassword());
        return cliente;
    }

}
