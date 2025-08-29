package com.sofka.banking.account.service;

import com.sofka.banking.account.dto.MovimientoDto;
import com.sofka.banking.account.entity.Cuenta;
import com.sofka.banking.account.entity.Movimiento;
import com.sofka.banking.account.exception.CuentaNotFoundException;
import com.sofka.banking.account.exception.SaldoInsuficienteException;
import com.sofka.banking.account.repository.CuentaRepository;
import com.sofka.banking.account.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Autowired
    public MovimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Registrar un movimiento (F2 y F3)
     * Esta es la funcionalidad clave que implementa las reglas de negocio
     */
    public MovimientoDto registrarMovimiento(MovimientoDto movimientoDto) {
        // Buscar la cuenta por número
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDto.getNumeroCuenta())
                .orElseThrow(() -> CuentaNotFoundException.porNumeroCuenta(movimientoDto.getNumeroCuenta()));

        // Validar que la cuenta esté activa
        if (!cuenta.getEstado()) {
            throw new IllegalStateException("La cuenta está inactiva: " + cuenta.getNumeroCuenta());
        }

        BigDecimal saldoActual = cuenta.getSaldoActual();
        BigDecimal valorMovimiento = movimientoDto.getValor();
        BigDecimal nuevoSaldo;

        // Calcular nuevo saldo según el tipo de movimiento
        if ("DEPOSITO".equals(movimientoDto.getTipoMovimiento())) {
            nuevoSaldo = saldoActual.add(valorMovimiento);
        } else if ("RETIRO".equals(movimientoDto.getTipoMovimiento())) {
            // F3: Validar saldo suficiente
            if (saldoActual.compareTo(valorMovimiento) < 0) {
                throw SaldoInsuficienteException.conMensaje(
                        "Saldo no disponible",
                        saldoActual,
                        valorMovimiento
                );
            }
            nuevoSaldo = saldoActual.subtract(valorMovimiento);
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no válido: " + movimientoDto.getTipoMovimiento());
        }

        // Crear el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(movimientoDto.getTipoMovimiento());
        movimiento.setValor(valorMovimiento);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setDescripcion(movimientoDto.getDescripcion());
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());

        // F2: Actualizar el saldo de la cuenta
        cuenta.actualizarSaldo(nuevoSaldo);

        // Guardar movimiento y cuenta
        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);
        cuentaRepository.save(cuenta);

        return convertirEntidadADto(movimientoGuardado);
    }

    /**
     * F1: Actualizar movimiento (solo descripción)
     */
    public MovimientoDto actualizarMovimiento(Long id, String nuevaDescripcion) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));

        movimiento.setDescripcion(nuevaDescripcion);
        Movimiento movimientoActualizado = movimientoRepository.save(movimiento);

        return convertirEntidadADto(movimientoActualizado);
    }

    /**
     * F1: Eliminar movimiento (solo para casos especiales)
     */
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));

        Cuenta cuenta = movimiento.getCuenta();
        if (cuenta == null) {
            throw new RuntimeException("No se encontró la cuenta asociada al movimiento");
        }

        // Revertir el saldo según el tipo de movimiento
        BigDecimal saldoActual = cuenta.getSaldoActual();
        BigDecimal valorMovimiento = movimiento.getValor();
        BigDecimal nuevoSaldo;
        if ("DEPOSITO".equals(movimiento.getTipoMovimiento())) {
            nuevoSaldo = saldoActual.subtract(valorMovimiento);
        } else if ("RETIRO".equals(movimiento.getTipoMovimiento())) {
            nuevoSaldo = saldoActual.add(valorMovimiento);
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no válido: " + movimiento.getTipoMovimiento());
        }

        cuenta.actualizarSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);
        movimientoRepository.deleteById(id);
    }

    /**
     * Obtener todos los movimientos
     */
    @Transactional(readOnly = true)
    public List<MovimientoDto> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::convertirEntidadADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener movimientos por número de cuenta
     */
    @Transactional(readOnly = true)
    public List<MovimientoDto> obtenerMovimientosPorCuenta(String numeroCuenta) {
        return movimientoRepository.findByNumeroCuentaOrderByFechaDesc(numeroCuenta)
                .stream()
                .map(this::convertirEntidadADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener movimientos por cliente y rango de fechas (para reportes)
     */
    @Transactional(readOnly = true)
    public List<MovimientoDto> obtenerMovimientosPorCliente(String clienteId) {
        return movimientoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirEntidadADto)
                .collect(Collectors.toList());
    }

    private MovimientoDto convertirEntidadADto(Movimiento movimiento) {
        MovimientoDto dto = new MovimientoDto();
        dto.setMovimientoId(movimiento.getMovimientoId());
        dto.setFecha(movimiento.getFecha());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento());
        dto.setValor(movimiento.getValor());
        dto.setSaldo(movimiento.getSaldo());
        dto.setDescripcion(movimiento.getDescripcion());
        dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        dto.setCuentaId(movimiento.getCuenta().getCuentaId());
        return dto;
    }
}
