package com.sofka.banking.account.controller;

import com.sofka.banking.account.dto.ReporteDto;
import com.sofka.banking.account.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * F4: Generar reporte de estado de cuenta por cliente y rango de fechas
     * GET /api/reportes/cliente/{clienteId}?fechaInicio=2023-01-01T00:00:00&fechaFin=2023-12-31T23:59:59
     *
     * Ejemplo de uso:
     * GET /api/reportes/cliente/CLI001?fechaInicio=2025-01-01T00:00:00&fechaFin=2025-12-31T23:59:59
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ReporteDto> generarReporteEstadoCuenta(
            @PathVariable String clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        ReporteDto reporte = reporteService.generarReporteEstadoCuenta(clienteId, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * F4: Generar reporte por cuenta específica
     * GET /api/reportes/cuenta/{numeroCuenta}?fechaInicio=2023-01-01T00:00:00&fechaFin=2023-12-31T23:59:59
     */
    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<ReporteDto> generarReportePorCuenta(
            @PathVariable String numeroCuenta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        ReporteDto reporte = reporteService.generarReportePorCuenta(numeroCuenta, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Generar reporte de movimientos recientes por cliente
     * GET /api/reportes/cliente/{clienteId}/recientes?dias=30
     */
    @GetMapping("/cliente/{clienteId}/recientes")
    public ResponseEntity<ReporteDto> generarReporteMovimientosRecientes(
            @PathVariable String clienteId,
            @RequestParam(defaultValue = "30") int dias) {

        ReporteDto reporte = reporteService.generarReporteMovimientosRecientes(clienteId, dias);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Obtener estadísticas de cuenta
     * GET /api/reportes/cuenta/{numeroCuenta}/estadisticas?fechaInicio=2023-01-01T00:00:00&fechaFin=2023-12-31T23:59:59
     */
    @GetMapping("/cuenta/{numeroCuenta}/estadisticas")
    public ResponseEntity<ReporteService.ReporteEstadisticasDto> obtenerEstadisticasCuenta(
            @PathVariable String numeroCuenta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        ReporteService.ReporteEstadisticasDto estadisticas = reporteService.obtenerEstadisticasCuenta(
                numeroCuenta, fechaInicio, fechaFin);
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Generar reporte del día actual por cliente
     * GET /api/reportes/cliente/{clienteId}/hoy
     */
    @GetMapping("/cliente/{clienteId}/hoy")
    public ResponseEntity<ReporteDto> generarReporteDelDia(@PathVariable String clienteId) {
        LocalDateTime inicioDelDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finDelDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        ReporteDto reporte = reporteService.generarReporteEstadoCuenta(clienteId, inicioDelDia, finDelDia);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Generar reporte de la semana actual por cliente
     * GET /api/reportes/cliente/{clienteId}/semana
     */
    @GetMapping("/cliente/{clienteId}/semana")
    public ResponseEntity<ReporteDto> generarReporteDeLaSemana(@PathVariable String clienteId) {
        LocalDateTime inicioSemana = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finSemana = LocalDateTime.now();

        ReporteDto reporte = reporteService.generarReporteEstadoCuenta(clienteId, inicioSemana, finSemana);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Generar reporte del mes actual por cliente
     * GET /api/reportes/cliente/{clienteId}/mes
     */
    @GetMapping("/cliente/{clienteId}/mes")
    public ResponseEntity<ReporteDto> generarReporteDelMes(@PathVariable String clienteId) {
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finMes = LocalDateTime.now();

        ReporteDto reporte = reporteService.generarReporteEstadoCuenta(clienteId, inicioMes, finMes);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Endpoint de salud del servicio de reportes
     * GET /api/reportes/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "account-service");
        health.put("module", "reportes");
        health.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(health);
    }

    /**
     * Obtener información general sobre reportes disponibles
     * GET /api/reportes/info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> informacionReportes() {
        Map<String, Object> info = new HashMap<>();
        info.put("reportesDisponibles", java.util.Arrays.asList(
                "Estado de cuenta por cliente y fechas",
                "Estado de cuenta por cuenta específica",
                "Movimientos recientes",
                "Estadísticas de cuenta",
                "Reportes predefinidos (hoy, semana, mes)"
        ));

        info.put("formatoFechas", "yyyy-MM-ddTHH:mm:ss (ISO 8601)");
        info.put("ejemploFecha", "2025-08-28T10:30:00");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("reporteCliente", "/api/reportes/cliente/{clienteId}?fechaInicio={fecha}&fechaFin={fecha}");
        endpoints.put("reporteCuenta", "/api/reportes/cuenta/{numeroCuenta}?fechaInicio={fecha}&fechaFin={fecha}");
        endpoints.put("reporteRecientes", "/api/reportes/cliente/{clienteId}/recientes?dias={dias}");
        endpoints.put("estadisticas", "/api/reportes/cuenta/{numeroCuenta}/estadisticas?fechaInicio={fecha}&fechaFin={fecha}");

        info.put("endpoints", endpoints);
        info.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok(info);
    }
}
