package com.sofka.banking.account.controller;


import com.sofka.banking.account.dto.CuentaDto;
import com.sofka.banking.account.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*")
public class CuentaController {
    private final CuentaService cuentaService;

    @Autowired
    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    /**
     * F1: Crear una nueva cuenta
     * POST /api/cuentas
     */
    @PostMapping
    public ResponseEntity<CuentaDto> crearCuenta(@Valid @RequestBody CuentaDto cuentaDto) {
        CuentaDto cuentaCreada = cuentaService.crearCuenta(cuentaDto);
        return new ResponseEntity<>(cuentaCreada, HttpStatus.CREATED);
    }

    /**
     * F1: Actualizar cuenta
     * PUT /api/cuentas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CuentaDto> actualizarCuenta(
            @PathVariable Long id,
            @Valid @RequestBody CuentaDto cuentaDto) {
        CuentaDto cuentaActualizada = cuentaService.actualizarCuenta(id, cuentaDto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    /**
     * F1: Actualizar cuenta por número de cuenta
     * PUT /api/cuentas/numero/{numeroCuenta}
     */
    @PutMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaDto> actualizarCuentaPorNumero(
            @PathVariable String numeroCuenta,
            @Valid @RequestBody CuentaDto cuentaDto) {
        CuentaDto cuentaActualizada = cuentaService.actualizarPorNumeroCuenta(numeroCuenta, cuentaDto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    /**
     * Cambiar estado de cuenta (activar/desactivar)
     * PATCH /api/cuentas/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CuentaDto> cambiarEstadoCuenta(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        Boolean estado = body.get("estado");
        CuentaDto cuentaActualizada = cuentaService.cambiarEstadoCuenta(id, estado);
        return ResponseEntity.ok(cuentaActualizada);
    }


    /**
     * F1: Eliminar cuenta (eliminación lógica)
     * DELETE /api/cuentas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarCuenta(@PathVariable Long id) {
        cuentaService.eliminarCuenta(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cuenta eliminada exitosamente (desactivada)");
        respuesta.put("cuentaId", id.toString());

        return ResponseEntity.ok(respuesta);
    }

    /**
     * F1: Eliminar cuenta físicamente (solo para testing)
     * DELETE /api/cuentas/{id}/fisico
     */
    @DeleteMapping("/{id}/fisico")
    public ResponseEntity<Map<String, String>> eliminarCuentaFisicamente(@PathVariable Long id) {
        Map<String, String> respuesta = new HashMap<>();
        try {
            cuentaService.eliminarCuentaFisicamente(id);
            respuesta.put("mensaje", "Cuenta eliminada físicamente de la base de datos");
            respuesta.put("cuentaId", id.toString());
            return ResponseEntity.ok(respuesta);
        } catch (IllegalStateException e) {
            respuesta.put("error", e.getMessage());
            respuesta.put("cuentaId", id.toString());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }

    /**
     * F1: Obtener todas las cuentas
     * GET /api/cuentas
     */
    @GetMapping
    public ResponseEntity<List<CuentaDto>> obtenerTodasLasCuentas() {
        List<CuentaDto> cuentas = cuentaService.obtenerTodasLasCuentas();
        return ResponseEntity.ok(cuentas);
    }


    /**
     * F1: Obtener cuenta por número de cuenta
     * GET /api/cuentas/numero/{numeroCuenta}
     */
    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaDto> obtenerCuentaPorNumero(@PathVariable String numeroCuenta) {
        CuentaDto cuenta = cuentaService.obtenerCuentaPorNumero(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    /**
     * F1: Obtener cuentas por cliente
     * GET /api/cuentas/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaDto>> obtenerCuentasPorCliente(@PathVariable String clienteId) {
        List<CuentaDto> cuentas = cuentaService.obtenerCuentasPorCliente(clienteId);
        return ResponseEntity.ok(cuentas);
    }

    /**
     * F1: Endpoint de salud del servicio
     * GET /api/cuentas/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "account-service");
        health.put("module", "cuentas");
        health.put("timestamp", java.time.LocalDateTime.now());

        return ResponseEntity.ok(health);
    }

}
