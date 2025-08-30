package com.sofka.banking.client.service;

import com.sofka.banking.client.dto.ClienteDto;
import com.sofka.banking.client.entity.Cliente;
import com.sofka.banking.client.exception.ClienteAlreadyExistsException;

import com.sofka.banking.client.exception.ClienteNotFoundException;
import com.sofka.banking.client.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setPersonaId(1L);
        cliente.setClienteId("CLI001");
        cliente.setNombre("José Soledispa");
        cliente.setGenero("Masculino");
        cliente.setEdad(28);
        cliente.setIdentificacion("2400036493");
        cliente.setDireccion("Calle 123");
        cliente.setTelefono("0987654321");
        cliente.setContrasena("password123");
        cliente.setEstado(true);

        clienteDto = new ClienteDto();
        clienteDto.setPersonaId(1L);
        clienteDto.setClienteId("CLI001");
        clienteDto.setNombre("José Soledispa");
        clienteDto.setGenero("Masculino");
        clienteDto.setEdad(28);
        clienteDto.setIdentificacion("2400036493");
        clienteDto.setDireccion("Calle 123");
        clienteDto.setTelefono("0987654321");
        clienteDto.setContrasena("password123");
        clienteDto.setEstado(true);
    }

    @Test
    @Order(1)
    void testCrearCliente_Exitoso() {

        when(clienteRepository.existsByClienteId(any(String.class))).thenReturn(false);
        when(clienteRepository.existsByIdentificacion(any(String.class))).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);


        ClienteDto resultado = clienteService.crearCliente(clienteDto);


        assertNotNull(resultado);
        assertEquals(cliente.getClienteId(), resultado.getClienteId());
        assertEquals(cliente.getNombre(), resultado.getNombre());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @Order(2)
    void testCrearCliente_ClienteYaExiste() {
       
        when(clienteRepository.existsByClienteId(cliente.getClienteId())).thenReturn(true);

        
        assertThrows(ClienteAlreadyExistsException.class, () ->
                clienteService.crearCliente(clienteDto));

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @Order(3)
    void testCrearCliente_IdentificacionYaExiste() {
        
        when(clienteRepository.existsByClienteId(cliente.getClienteId())).thenReturn(false);
        when(clienteRepository.existsByIdentificacion(cliente.getIdentificacion())).thenReturn(true);

        
        assertThrows(ClienteAlreadyExistsException.class, () ->
                clienteService.crearCliente(clienteDto));

        verify(clienteRepository, never()).save(any());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1:CLI002:Jose Soledisa:Masculino:28:2400036493:Barrio 1:0987654321:admin:true",
            "2:CLI001:Jose Soledisa:Masculino:28:2400036494:Barrio 1:0987654321:admin:null",
            "3:CLI001:Jose Soledisa:Masculino:28:2400036493:Barrio 1:0987654321:null:true",

    }, delimiter = ':')
    @Order(4)
    void testActualizarCliente_Exitoso(Long idPersona,String idCliente,String nombre,String genero,int edad,
                                       String identificacion , String direccion,String telefono, String contrasena,boolean estado)  {

        when(clienteRepository.findById(cliente.getPersonaId())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDto updateDto = new ClienteDto();
        updateDto.setPersonaId(idPersona);
        updateDto.setClienteId(idCliente);
        updateDto.setNombre(nombre);
        updateDto.setGenero(genero);
        updateDto.setEdad(edad);
        updateDto.setIdentificacion(identificacion);
        updateDto.setDireccion(direccion);
        updateDto.setTelefono(telefono);
        updateDto.setContrasena(contrasena);
        updateDto.setEstado(estado);


        ClienteDto resultado = clienteService.actualizarCliente(cliente.getPersonaId(), updateDto);


        assertNotNull(resultado);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "CLI002",
            "CLI003"
    })
    @Order(5)
    void testActualizarCliente_ClienteIdExiste(String idCliente) {
        
        clienteDto.setClienteId(idCliente);
        when(clienteRepository.existsByClienteId(any(String.class))).thenReturn(true);
        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.of(cliente));

        
        assertThrows(ClienteAlreadyExistsException.class, () -> {
            clienteService.actualizarCliente(1L, clienteDto);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {
            "2400036494"
    })
    @Order(6)
    void testActualizarCliente_IdentificacionExiste(String identificacion) {
       
        clienteDto.setIdentificacion(identificacion);
        when(clienteRepository.existsByIdentificacion(any(String.class))).thenReturn(true);
        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.of(cliente));

        
        assertThrows(ClienteAlreadyExistsException.class, () -> {
            clienteService.actualizarCliente(1L, clienteDto);
        });
    }

    @Test
    @Order(7)
    void testEliminarCliente_Exitoso() {
        Long id = 1L;

       
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        cliente.setEstado(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        
        assertDoesNotThrow(() -> clienteService.eliminarCliente(id));
        verify(clienteRepository).save(any(Cliente.class));
        assertFalse(cliente.getEstado());
    }

    @Test
    @Order(8)
    void testEliminarCliente_NoEncontrado() {
       
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

       
        assertThrows(ClienteNotFoundException.class, () ->
                clienteService.eliminarCliente(1L));
    }

    @Test
    @Order(9)
    void testEliminarClienteFisicamente_Exitoso() {
        Long id = 1L;
        when(clienteRepository.existsById(id)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(id);

        assertDoesNotThrow(() -> clienteService.eliminarClienteFisicamente(id));
        verify(clienteRepository).deleteById(id);
    }

    @Test
    @Order(10)
    void testEliminarClienteFisicamente_ClienteNoExiste() {
        Long id = 2L;
        when(clienteRepository.existsById(id)).thenReturn(false);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.eliminarClienteFisicamente(id));
    }

    @Test
    @Order(11)
    void testObtenerClientePorClienteId_Exitoso() {
        String clienteId = "CLI001";
        when(clienteRepository.findByClienteId(clienteId)).thenReturn(Optional.of(cliente));

        ClienteDto resultado = clienteService.obtenerClientePorClienteId(clienteId);
        assertNotNull(resultado);
        assertEquals(clienteId, resultado.getClienteId());
    }

    @Test
    @Order(12)
    void testObtenerClientePorClienteId_NoExiste() {
        String clienteId = "CLI999";
        when(clienteRepository.findByClienteId(clienteId)).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> clienteService.obtenerClientePorClienteId(clienteId));
    }

    @Test
    @Order(13)
    void testObtenerTodosLosClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        List<ClienteDto> resultado = clienteService.obtenerTodosLosClientes();
        assertFalse(resultado.isEmpty());
        assertEquals(cliente.getClienteId(), resultado.get(0).getClienteId());
    }

    @Test
    @Order(14)
    void testCambiarEstadoCliente_Exitoso() {
        Long id = 1L;
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        cliente.setEstado(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteDto resultado = clienteService.cambiarEstadoCliente(id, false);
        assertNotNull(resultado);
        assertFalse(resultado.getEstado());
    }

    @Test
    @Order(15)
    void testCambiarEstadoCliente_ClienteNoExiste() {
        Long id = 2L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> clienteService.cambiarEstadoCliente(id, true));
    }

}
