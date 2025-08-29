package com.sofka.banking.account.service;

import com.sofka.banking.account.dto.ReporteDto;
import com.sofka.banking.account.entity.Cuenta;
import com.sofka.banking.account.entity.Movimiento;
import com.sofka.banking.account.exception.ClienteNotFoundException;
import com.sofka.banking.account.exception.CuentaNotFoundException;
import com.sofka.banking.account.repository.CuentaRepository;
import com.sofka.banking.account.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${microservices.client-service.url}")
    private String clientServiceUrl;

    @Autowired
    public ReporteService(CuentaRepository cuentaRepository,
                          MovimientoRepository movimientoRepository,
                          WebClient.Builder webClientBuilder) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.webClientBuilder = webClientBuilder;
    }


    /**
     * F4: Generar reporte de estado de cuenta por cliente y rango de fechas
     */
    public ReporteDto generarReporteEstadoCuenta(String clienteId,
                                                 LocalDateTime fechaInicio,
                                                 LocalDateTime fechaFin) {

        // Validar que las fechas sean válidas
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        // Obtener información del cliente
        String nombreCliente = obtenerNombreCliente(clienteId);

        // Obtener todas las cuentas del cliente
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        if (cuentas.isEmpty()) {
            throw CuentaNotFoundException.porCliente(clienteId);
        }

        // Crear el reporte
        ReporteDto reporte = new ReporteDto(clienteId, nombreCliente, fechaInicio, fechaFin);

        // Procesar cada cuenta
        List<ReporteDto.CuentaReporteDto> cuentasReporte = new ArrayList<>();

        for (Cuenta cuenta : cuentas) {
            ReporteDto.CuentaReporteDto cuentaReporte = new ReporteDto.CuentaReporteDto(
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipoCuenta(),
                    cuenta.getSaldoInicial(),
                    cuenta.getSaldoActual(),
                    cuenta.getEstado()
            );

            // Obtener movimientos de la cuenta en el rango de fechas
            List<Movimiento> movimientos = movimientoRepository.findMovimientosParaReporte(
                    cuenta.getNumeroCuenta(), fechaInicio, fechaFin);

            // Convertir movimientos a DTOs para el reporte
            List<ReporteDto.MovimientoReporteDto> movimientosReporte = movimientos.stream()
                    .map(this::convertirMovimientoAReporteDto)
                    .collect(Collectors.toList());

            cuentaReporte.setMovimientos(movimientosReporte);
            cuentasReporte.add(cuentaReporte);
        }

        reporte.setCuentas(cuentasReporte);
        return reporte;
    }

    /**
     * F4: Generar reporte simplificado por cuenta específica
     */
    public ReporteDto generarReportePorCuenta(String numeroCuenta,
                                              LocalDateTime fechaInicio,
                                              LocalDateTime fechaFin) {

        // Buscar la cuenta
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> CuentaNotFoundException.porNumeroCuenta(numeroCuenta));

        // Obtener información del cliente
        String nombreCliente = obtenerNombreCliente(cuenta.getClienteId());

        // Crear reporte para una sola cuenta
        ReporteDto reporte = new ReporteDto(cuenta.getClienteId(), nombreCliente, fechaInicio, fechaFin);

        ReporteDto.CuentaReporteDto cuentaReporte = new ReporteDto.CuentaReporteDto(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoActual(),
                cuenta.getEstado()
        );

        // Obtener movimientos de la cuenta en el rango de fechas
        List<Movimiento> movimientos = movimientoRepository.findMovimientosParaReporte(
                numeroCuenta, fechaInicio, fechaFin);

        List<ReporteDto.MovimientoReporteDto> movimientosReporte = movimientos.stream()
                .map(this::convertirMovimientoAReporteDto)
                .collect(Collectors.toList());

        cuentaReporte.setMovimientos(movimientosReporte);

        List<ReporteDto.CuentaReporteDto> cuentas = new ArrayList<>();
        cuentas.add(cuentaReporte);
        reporte.setCuentas(cuentas);

        return reporte;
    }

    /**
     * Obtener estadísticas de cuenta
     */
    public ReporteEstadisticasDto obtenerEstadisticasCuenta(String numeroCuenta,
                                                            LocalDateTime fechaInicio,
                                                            LocalDateTime fechaFin) {

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> CuentaNotFoundException.porNumeroCuenta(numeroCuenta));

        List<Movimiento> movimientos = movimientoRepository.findMovimientosParaReporte(
                numeroCuenta, fechaInicio, fechaFin);

        return calcularEstadisticas(cuenta, movimientos);
    }


    /**
     * Generar reporte de movimientos recientes por cliente
     */
    public ReporteDto generarReporteMovimientosRecientes(String clienteId, int ultimosDias) {
        LocalDateTime fechaFin = LocalDateTime.now();
        LocalDateTime fechaInicio = fechaFin.minusDays(ultimosDias);

        return generarReporteEstadoCuenta(clienteId, fechaInicio, fechaFin);
    }

    /**
     * Obtener el nombre del cliente desde el microservicio de clientes
     */
    private String obtenerNombreCliente(String clienteId) {
        try {
            WebClient webClient = webClientBuilder.build();

            ClienteResponse cliente = webClient.get()
                    .uri(clientServiceUrl + "/api/clientes/clienteId/" + clienteId)
                    .retrieve()
                    .bodyToMono(ClienteResponse.class)
                    .block();

            return cliente != null ? cliente.getNombre() : "Cliente " + clienteId;

        } catch (Exception e) {
            throw ClienteNotFoundException.porClienteId(clienteId);
        }
    }


    /**
     * Convertir movimiento a DTO de reporte
     */
    private ReporteDto.MovimientoReporteDto convertirMovimientoAReporteDto(Movimiento movimiento) {
        return new ReporteDto.MovimientoReporteDto(
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.getDescripcion()
        );
    }

    // Clases auxiliares para respuestas

    public static class ClienteResponse {
        private String nombre;
        private String clienteId;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getClienteId() { return clienteId; }
        public void setClienteId(String clienteId) { this.clienteId = clienteId; }
    }

    public static class ReporteEstadisticasDto {
        private String numeroCuenta;
        private java.math.BigDecimal saldoActual;
        private int totalMovimientos;
        private long totalDepositos;
        private long totalRetiros;

        // Getters y Setters
        public String getNumeroCuenta() { return numeroCuenta; }
        public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

        public java.math.BigDecimal getSaldoActual() { return saldoActual; }
        public void setSaldoActual(java.math.BigDecimal saldoActual) { this.saldoActual = saldoActual; }

        public int getTotalMovimientos() { return totalMovimientos; }
        public void setTotalMovimientos(int totalMovimientos) { this.totalMovimientos = totalMovimientos; }

        public long getTotalDepositos() { return totalDepositos; }
        public void setTotalDepositos(long totalDepositos) { this.totalDepositos = totalDepositos; }

        public long getTotalRetiros() { return totalRetiros; }
        public void setTotalRetiros(long totalRetiros) { this.totalRetiros = totalRetiros; }
    }

    /**
     * Calcular estadísticas de una cuenta
     */
    private ReporteEstadisticasDto calcularEstadisticas(Cuenta cuenta, List<Movimiento> movimientos) {
        ReporteEstadisticasDto estadisticas = new ReporteEstadisticasDto();
        estadisticas.setNumeroCuenta(cuenta.getNumeroCuenta());
        estadisticas.setSaldoActual(cuenta.getSaldoActual());
        estadisticas.setTotalMovimientos(movimientos.size());

        long depositos = movimientos.stream()
                .filter(m -> "DEPOSITO".equals(m.getTipoMovimiento()))
                .count();

        long retiros = movimientos.stream()
                .filter(m -> "RETIRO".equals(m.getTipoMovimiento()))
                .count();

        estadisticas.setTotalDepositos(depositos);
        estadisticas.setTotalRetiros(retiros);

        return estadisticas;
    }
}
