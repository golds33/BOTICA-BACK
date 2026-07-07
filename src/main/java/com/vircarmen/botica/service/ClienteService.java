package com.vircarmen.botica.service;

import com.vircarmen.botica.dto.ClienteDTO;
import com.vircarmen.botica.dto.ClienteRequest;
import com.vircarmen.botica.entity.Cliente;
import com.vircarmen.botica.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteDTO buscarPorDocumento(String numeroDocumento) {
        Cliente c = clienteRepository.findByNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con documento: " + numeroDocumento));
        return mapToDTO(c);
    }

    public ClienteDTO registrarCliente(ClienteRequest request) {
        // Validar si ya existe
        if (clienteRepository.findByNumeroDocumento(request.numeroDocumento()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con ese número de documento");
        }

        Cliente cliente = new Cliente();
        cliente.setTipoDocumento(request.tipoDocumento());
        cliente.setNumeroDocumento(request.numeroDocumento());
        cliente.setNombreRazonSocial(request.nombreRazonSocial());
        cliente.setDireccion(request.direccion());

        Cliente guardado = clienteRepository.save(cliente);
        return mapToDTO(guardado);
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ClienteDTO mapToDTO(Cliente c) {
        return new ClienteDTO(
                c.getIdCliente(),
                c.getTipoDocumento(),
                c.getNumeroDocumento(),
                c.getNombreRazonSocial()
        );
    }

    @Transactional
    public ClienteDTO actualizarCliente(Integer idCliente, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(Integer.valueOf(idCliente))
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setTipoDocumento(request.tipoDocumento());
        cliente.setNumeroDocumento(request.numeroDocumento());
        cliente.setNombreRazonSocial(request.nombreRazonSocial());
        cliente.setDireccion(request.direccion());

        return mapToDTO(clienteRepository.save(cliente));
    }
}