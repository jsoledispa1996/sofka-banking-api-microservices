package com.sofka.banking.client.service;


import com.sofka.banking.client.dto.ClienteDto;
import com.sofka.banking.client.entity.Cliente;
import com.sofka.banking.client.exception.ClienteAlreadyExistsException;
import com.sofka.banking.client.exception.ClienteNotFoundException;
import com.sofka.banking.client.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Crear un nuevo cliente
     */
    public ClienteDto crearCliente(ClienteDto clienteDto) {
        // Validar que no exista un cliente con el mismo clienteId
        if (clienteRepository.existsByClienteId(clienteDto.getClienteId())) {
            throw ClienteAlreadyExistsException.porClienteId(clienteDto.getClienteId());
        }

        // Validar que no exista una persona con la misma identificación
        if (clienteRepository.existsByIdentificacion(clienteDto.getIdentificacion())) {
            throw ClienteAlreadyExistsException.porIdentificacion(clienteDto.getIdentificacion());
        }

        Cliente cliente = convertirDtoAEntidad(clienteDto);
        cliente.setEstado(true); // Por defecto activo

        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirEntidadADto(clienteGuardado);
    }

    /**
     * Actualizar cliente
     */
    public ClienteDto actualizarCliente(Long id, ClienteDto clienteDto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> ClienteNotFoundException.porId(id));

        // Validar que no exista otro cliente con el mismo clienteId (si se está cambiando)
        if (!clienteExistente.getClienteId().equals(clienteDto.getClienteId()) &&
                clienteRepository.existsByClienteId(clienteDto.getClienteId())) {
            throw ClienteAlreadyExistsException.porClienteId(clienteDto.getClienteId());
        }

        // Validar que no exista otra persona con la misma identificación (si se está cambiando)
        if (!clienteExistente.getIdentificacion().equals(clienteDto.getIdentificacion()) &&
                clienteRepository.existsByIdentificacion(clienteDto.getIdentificacion())) {
            throw ClienteAlreadyExistsException.porIdentificacion(clienteDto.getIdentificacion());
        }

        // Actualizar campos
        actualizarCamposCliente(clienteExistente, clienteDto);

        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        return convertirEntidadADto(clienteActualizado);
    }

    /**
     * Eliminar cliente (eliminación lógica)
     */
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> ClienteNotFoundException.porId(id));

        cliente.setEstado(false); // Eliminación lógica
        clienteRepository.save(cliente);
    }

    /**
     * Eliminar cliente físicamente
     */
    public void eliminarClienteFisicamente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw ClienteNotFoundException.porId(id);
        }
        clienteRepository.deleteById(id);
    }

    /**
     * Obtener cliente por ClienteId
     */
    @Transactional(readOnly = true)
    public ClienteDto obtenerClientePorClienteId(String clienteId) {
        Cliente cliente = clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> ClienteNotFoundException.porClienteId(clienteId));
        return convertirEntidadADto(cliente);
    }

    /**
     * Obtener todos los clientes
     */
    @Transactional(readOnly = true)
    public List<ClienteDto> obtenerTodosLosClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertirEntidadADto)
                .collect(Collectors.toList());
    }

    /**
     * Activar/Desactivar cliente
     */
    public ClienteDto cambiarEstadoCliente(Long id, Boolean estado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> ClienteNotFoundException.porId(id));

        cliente.setEstado(estado);
        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertirEntidadADto(clienteActualizado);
    }

    private Cliente convertirDtoAEntidad(ClienteDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setClienteId(dto.getClienteId());
        cliente.setContrasena(dto.getContrasena());
        cliente.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        return cliente;
    }

    private ClienteDto convertirEntidadADto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setPersonaId(cliente.getPersonaId());
        dto.setNombre(cliente.getNombre());
        dto.setGenero(cliente.getGenero());
        dto.setEdad(cliente.getEdad());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        dto.setClienteId(cliente.getClienteId());
        dto.setContrasena(cliente.getContrasena());
        dto.setEstado(cliente.getEstado());
        return dto;
    }

    private void actualizarCamposCliente(Cliente cliente, ClienteDto dto) {
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setClienteId(dto.getClienteId());

        // Solo actualizar contraseña si se proporciona una nueva
        if (dto.getContrasena() != null && !dto.getContrasena().trim().isEmpty()) {
            cliente.setContrasena(dto.getContrasena());
        }

        if (dto.getEstado() != null) {
            cliente.setEstado(dto.getEstado());
        }
    }
}
