package com.sofka.banking.account.controller;

import com.sofka.banking.account.dto.MovimientoDto;
import com.sofka.banking.account.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Autowired
    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    /**
     * F2 y F3: Registrar un movimiento (depósito o retiro)
     * POST /api/movimientos
     *
     * Ejemplo de JSON para depósito:
     * {
     *   "tipoMovimiento": "DEPOSITO",
     *   "valor": 100.00,
     *   "numeroCuenta": "478758",
     *   "descripcion": "Depósito en efectivo"
     * }
     *
     * Ejemplo de JSON para retiro:
     * {
     *   "tipoMovimiento": "RETIRO",
     *   "valor": 50.00,
     *   "numeroCuenta": "478758",
     *   "descripcion": "Retiro cajero automático"
     * }
     */
    @PostMapping
    public ResponseEntity<MovimientoDto> registrarMovimiento(@Valid @RequestBody MovimientoDto movimientoDto) {
        MovimientoDto movimientoCreado = movimientoService.registrarMovimiento(movimientoDto);
        return new ResponseEntity<>(movimientoCreado, HttpStatus.CREATED);
    }

    /**
     * F1: Actualizar descripción de un movimiento
     * PATCH /api/movimientos/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<MovimientoDto> actualizarMovimiento(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates) {

        String nuevaDescripcion = updates.get("descripcion");
        if (nuevaDescripcion == null) {
            return ResponseEntity.badRequest().build();
        }

        MovimientoDto movimientoActualizado = movimientoService.actualizarMovimiento(id, nuevaDescripcion);
        return ResponseEntity.ok(movimientoActualizado);
    }

    /**
     * F1: Eliminar movimiento (solo para casos especiales)
     * DELETE /api/movimientos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Movimiento eliminado ( Se actualizo el  saldo de cuenta )");
        respuesta.put("movimientoId", id.toString());

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Obtener movimientos por número de cuenta
     * GET /api/movimientos/cuenta/{numeroCuenta}
     */
    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<List<MovimientoDto>> obtenerMovimientosPorCuenta(@PathVariable String numeroCuenta) {
        List<MovimientoDto> movimientos = movimientoService.obtenerMovimientosPorCuenta(numeroCuenta);
        return ResponseEntity.ok(movimientos);
    }

    /**
     * Obtener todos los movimientos
     * GET /api/movimientos
     */
    @GetMapping
    public ResponseEntity<List<MovimientoDto>> obtenerTodosLosMovimientos() {
        List<MovimientoDto> movimientos = movimientoService.obtenerTodosLosMovimientos();
        return ResponseEntity.ok(movimientos);
    }

    /**
     * Obtener movimientos por cliente
     * GET /api/movimientos/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<MovimientoDto>> obtenerMovimientosPorCliente(
            @PathVariable String clienteId) {

        List<MovimientoDto> movimientos = movimientoService.obtenerMovimientosPorCliente(
                clienteId);
        return ResponseEntity.ok(movimientos);
    }


    /**
     * Endpoint de salud del servicio de movimientos
     * GET /api/movimientos/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "account-service");
        health.put("module", "movimientos");
        health.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(health);
    }
}
