package com.sofka.banking.client.controller;


import com.sofka.banking.client.dto.ClienteDto;
import com.sofka.banking.client.service.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    /**
     * F1: Crear un nuevo cliente
     * POST /api/clientes
     */
    @PostMapping
    public ResponseEntity<ClienteDto> crearCliente(@Valid @RequestBody ClienteDto clienteDto) {
        ClienteDto clienteCreado = clienteService.crearCliente(clienteDto);
        return new ResponseEntity<>(clienteCreado, HttpStatus.CREATED);
    }

    /**
     * F1: Actualizar cliente
     * PUT /api/clientes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDto clienteDto) {
        ClienteDto clienteActualizado = clienteService.actualizarCliente(id, clienteDto);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * F1: Actualizar parcialmente un cliente
     * PATCH /api/clientes/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizarParcialmenteCliente(
            @PathVariable Long id,
            @RequestBody ClienteDto clienteDto) {
        // Para actualizaciones parciales, no validamos todos los campos
        ClienteDto clienteActualizado = clienteService.actualizarCliente(id, clienteDto);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * Cambiar estado del cliente (activar/desactivar)
     * PATCH /api/clientes/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ClienteDto> cambiarEstadoCliente(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        Boolean estado = body.get("estado");
        ClienteDto clienteActualizado = clienteService.cambiarEstadoCliente(id, estado);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * F1: Eliminar cliente (eliminación lógica)
     * DELETE /api/clientes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cliente eliminado exitosamente (desactivado)");
        respuesta.put("clienteId", id.toString());

        return ResponseEntity.ok(respuesta);
    }

    /**
     * F1: Eliminar cliente físicamente (solo para testing o casos especiales)
     * DELETE /api/clientes/{id}/fisico
     */
    @DeleteMapping("/{id}/fisico")
    public ResponseEntity<Map<String, String>> eliminarClienteFisicamente(@PathVariable Long id) {
        clienteService.eliminarClienteFisicamente(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cliente eliminado físicamente de la base de datos");
        respuesta.put("clienteId", id.toString());

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Obtener cliente por ClienteId
     * GET /api/clientes/clienteId/{clienteId}
     */
    @GetMapping("/clienteId/{clienteId}")
    public ResponseEntity<ClienteDto> obtenerClientePorClienteId(@PathVariable String clienteId) {
        ClienteDto cliente = clienteService.obtenerClientePorClienteId(clienteId);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Obtener todos los clientes
     * GET /api/clientes
     */
    @GetMapping
    public ResponseEntity<List<ClienteDto>> obtenerTodosLosClientes() {
        List<ClienteDto> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Endpoint de salud del servicio
     * GET /api/clientes/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "client-service");
        health.put("timestamp", java.time.LocalDateTime.now());

        return ResponseEntity.ok(health);
    }
}
