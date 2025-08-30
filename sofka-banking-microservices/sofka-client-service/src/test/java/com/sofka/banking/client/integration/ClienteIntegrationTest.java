package com.sofka.banking.client.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.banking.client.ClientServiceApplication;
import com.sofka.banking.client.dto.ClienteDto;
import com.sofka.banking.client.entity.Cliente;
import com.sofka.banking.client.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ClientServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class ClienteIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();

        clienteDto = new ClienteDto();
        clienteDto.setClienteId("CLI001");
        clienteDto.setNombre("José Soedispa");
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
    void testCrearCliente_FlujCompleto() throws Exception {

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value("CLI001"))
                .andExpect(jsonPath("$.nombre").value("José Soedispa"))
                .andExpect(jsonPath("$.identificacion").value("2400036493"))
                .andExpect(jsonPath("$.estado").value(true));

        assert clienteRepository.existsByClienteId("CLI001");
    }

    @Test
    @Order(2)
    void tesActualizarCliente_FlujCompleto() throws Exception {

        Cliente cliente = new Cliente();
        cliente.setClienteId("CLI010");
        cliente.setNombre("José Soledispa");
        cliente.setGenero("Masculino");
        cliente.setEdad(28);
        cliente.setIdentificacion("2400036410");
        cliente.setDireccion("Calle 123");
        cliente.setTelefono("0987654321");
        cliente.setContrasena("password123");
        cliente.setEstado(true);
        Cliente clienteGuardado =clienteRepository.save(cliente);
        Long id = clienteGuardado.getPersonaId();

        ClienteDto clienteDto1 = new ClienteDto();
        clienteDto1.setClienteId("CLI001");
        clienteDto1.setNombre("José Soedispa Yagual");
        clienteDto1.setGenero("Masculino");
        clienteDto1.setEdad(28);
        clienteDto1.setIdentificacion("2400036410");
        clienteDto1.setDireccion("Velazco Ibarra");
        clienteDto1.setTelefono("0987654321");
        clienteDto1.setContrasena("password123");
        clienteDto1.setEstado(false);

        mockMvc.perform(put("/api/clientes/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("CLI001"))
                .andExpect(jsonPath("$.nombre").value("José Soedispa Yagual"))
                .andExpect(jsonPath("$.identificacion").value("2400036410"))
                .andExpect(jsonPath("$.direccion").value("Velazco Ibarra"))
                .andExpect(jsonPath("$.estado").value(false));

        assert clienteRepository.existsByClienteId("CLI001");
    }

    @Test
    @Order(3)
    void testObtenerCliente_Existente() throws Exception {
       
        Cliente cliente = new Cliente();
        cliente.setClienteId("CLI001");
        cliente.setNombre("José Soledispa");
        cliente.setGenero("Masculino");
        cliente.setEdad(28);
        cliente.setIdentificacion("2400036493");
        cliente.setDireccion("Calle 123");
        cliente.setTelefono("0987654321");
        cliente.setContrasena("password123");
        cliente.setEstado(true);
        clienteRepository.save(cliente);

    
        mockMvc.perform(get("/api/clientes/clienteId/CLI001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("CLI001"))
                .andExpect(jsonPath("$.nombre").value("José Soledispa"));
    }

    @Test
    @Order(4)
    void testObtenerTodosLosClientes() throws Exception {
    
        Cliente cliente1 = new Cliente();
        cliente1.setClienteId("CLI001");
        cliente1.setNombre("José Soledispa");
        cliente1.setGenero("Masculino");
        cliente1.setEdad(28);
        cliente1.setIdentificacion("2400036493");
        cliente1.setDireccion("Calle 123");
        cliente1.setTelefono("0987654321");
        cliente1.setContrasena("password123");
        cliente1.setEstado(true);

        Cliente cliente2 = new Cliente();
        cliente2.setClienteId("CLI002");
        cliente2.setNombre("Pedro Guale");
        cliente2.setGenero("Masculino");
        cliente2.setEdad(28);
        cliente2.setIdentificacion("2400036494");
        cliente2.setDireccion("Calle 123");
        cliente2.setTelefono("0987654321");
        cliente2.setContrasena("password123");
        cliente2.setEstado(true);

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);

    
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].clienteId").value("CLI001"))
                .andExpect(jsonPath("$[1].clienteId").value("CLI002"));
    }
}
