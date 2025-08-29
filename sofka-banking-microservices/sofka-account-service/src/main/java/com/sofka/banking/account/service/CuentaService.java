
package com.sofka.banking.account.service;

import com.sofka.banking.account.dto.CuentaDto;
import com.sofka.banking.account.entity.Cuenta;
import com.sofka.banking.account.exception.ClienteNotFoundException;
import com.sofka.banking.account.exception.CuentaAlreadyExistsException;
import com.sofka.banking.account.exception.CuentaNotFoundException;
import com.sofka.banking.account.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${microservices.client-service.url}")
    private String clientServiceUrl;

    @Autowired
    public CuentaService(CuentaRepository cuentaRepository, WebClient.Builder webClientBuilder) {
        this.cuentaRepository = cuentaRepository;
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Crear una nueva cuenta
     */
    public CuentaDto crearCuenta(CuentaDto cuentaDto) {
        // Validar que no exista una cuenta con el mismo número
        if (cuentaRepository.existsByNumeroCuenta(cuentaDto.getNumeroCuenta())) {
            throw CuentaAlreadyExistsException.porNumeroCuenta(cuentaDto.getNumeroCuenta());
        }

        // Validar que el cliente existe en el microservicio de clientes
        validarExistenciaCliente(cuentaDto.getClienteId());

        Cuenta cuenta = convertirDtoAEntidad(cuentaDto);
        cuenta.setEstado(true);
        cuenta.setFechaCreacion(LocalDateTime.now());

        // El saldo actual inicialmente es igual al saldo inicial
        cuenta.setSaldoActual(cuenta.getSaldoInicial());

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
        return convertirEntidadADto(cuentaGuardada);
    }

    /**
     * Actualizar cuenta
     */
    public CuentaDto actualizarCuenta(Long id, CuentaDto cuentaDto) {
        Cuenta cuentaExistente = cuentaRepository.findById(id)
                .orElseThrow(() -> CuentaNotFoundException.porId(id));

        // Validar cliente si se está cambiando
        if (!cuentaExistente.getClienteId().equals(cuentaDto.getClienteId())) {
            validarExistenciaCliente(cuentaDto.getClienteId());
        }

        // Actualizar campos permitidos (no se puede cambiar saldo actual aquí)
        actualizarCamposCuenta(cuentaExistente, cuentaDto);

        Cuenta cuentaActualizada = cuentaRepository.save(cuentaExistente);
        return convertirEntidadADto(cuentaActualizada);
    }

    /**
     * Actualizar cuenta por número de cuenta
     */
    public CuentaDto actualizarPorNumeroCuenta(String numeroCuenta, CuentaDto cuentaDto) {
        Cuenta cuentaExistente = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> CuentaNotFoundException.porNumeroCuenta(numeroCuenta));

        // Validar cliente si se está cambiando
        if (!cuentaExistente.getClienteId().equals(cuentaDto.getClienteId())) {
            validarExistenciaCliente(cuentaDto.getClienteId());
        }
        // Actualizar campos permitidos (no se puede cambiar saldo actual aquí)
        actualizarCamposCuenta(cuentaExistente, cuentaDto);

        Cuenta cuentaActualizada = cuentaRepository.save(cuentaExistente);
        return convertirEntidadADto(cuentaActualizada);
    }

    /**
     * Cambiar estado de cuenta (activar/desactivar)
     */
    public CuentaDto cambiarEstadoCuenta(Long id, Boolean estado) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> CuentaNotFoundException.porId(id));

        cuenta.setEstado(estado);
        cuenta.setFechaActualizacion(LocalDateTime.now());

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);
        return convertirEntidadADto(cuentaActualizada);
    }


    /**
     * Eliminar cuenta (eliminación lógica)
     */
    public void eliminarCuenta(Long id) {

        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> CuentaNotFoundException.porId(id));

        cuenta.setEstado(false);
        cuenta.setFechaActualizacion(LocalDateTime.now());
        cuentaRepository.save(cuenta);
    }

    /**
     * Eliminar cuenta físicamente
     */
    public void eliminarCuentaFisicamente(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> CuentaNotFoundException.porId(id));

        // Verificar si existen movimientos asociados
        if (cuenta.getMovimientos() != null && !cuenta.getMovimientos().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar la cuenta porque tiene movimientos asociados.");
        }
        cuentaRepository.deleteById(id);
    }

    /**
     * Obtener cuenta por número de cuenta
     */
    @Transactional(readOnly = true)
    public CuentaDto obtenerCuentaPorNumero(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> CuentaNotFoundException.porNumeroCuenta(numeroCuenta));
        return convertirEntidadADto(cuenta);
    }

    /**
     * Obtener todas las cuentas
     */
    @Transactional(readOnly = true)
    public List<CuentaDto> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(this::convertirEntidadADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener cuentas por cliente
     */
    @Transactional(readOnly = true)
    public List<CuentaDto> obtenerCuentasPorCliente(String clienteId) {
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        if (cuentas.isEmpty()) {
            throw CuentaNotFoundException.porCliente(clienteId);
        }
        return cuentas.stream()
                .map(this::convertirEntidadADto)
                .collect(Collectors.toList());
    }

    /**
     * Validar si el cliente existe en el microservicio de clientes
     */
    private void validarExistenciaCliente(String clienteId) {
        try {
            WebClient webClient = webClientBuilder.build();

            Mono<String> response = webClient.get()
                    .uri(clientServiceUrl + "/api/clientes/clienteId/" + clienteId)
                    .retrieve()
                    .bodyToMono(String.class);

            // Si no existe, se lanzará una excepción
            response.block();

        } catch (Exception e) {
            throw ClienteNotFoundException.porClienteId(clienteId);
        }
    }

    // Métodos auxiliares

    private Cuenta convertirDtoAEntidad(CuentaDto dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldoInicial(dto.getSaldoInicial());
        cuenta.setSaldoActual(dto.getSaldoInicial()); // Inicialmente igual al saldo inicial
        cuenta.setClienteId(dto.getClienteId());
        cuenta.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        return cuenta;
    }

    private CuentaDto convertirEntidadADto(Cuenta cuenta) {
        CuentaDto dto = new CuentaDto();
        dto.setCuentaId(cuenta.getCuentaId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setSaldoActual(cuenta.getSaldoActual());
        dto.setEstado(cuenta.getEstado());
        dto.setClienteId(cuenta.getClienteId());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        dto.setFechaActualizacion(cuenta.getFechaActualizacion());
        return dto;
    }

    private void actualizarCamposCuenta(Cuenta cuenta, CuentaDto dto) {
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setClienteId(dto.getClienteId());

        // Solo actualizar saldo inicial si se proporciona y es diferente
        if (dto.getSaldoInicial() != null) {
            cuenta.setSaldoInicial(dto.getSaldoInicial());
        }

        if (dto.getEstado() != null) {
            cuenta.setEstado(dto.getEstado());
        }

        cuenta.setFechaActualizacion(LocalDateTime.now());
    }

}
