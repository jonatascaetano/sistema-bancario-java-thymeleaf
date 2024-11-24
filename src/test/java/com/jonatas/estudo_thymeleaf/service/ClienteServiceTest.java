package com.jonatas.estudo_thymeleaf.service;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContaBancariaService contaBancariaService;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_ShouldReturnListOfClientes() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        var result = clienteService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnCliente_WhenExists() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> clienteService.findById(1L));
        assertEquals("Cliente com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    void save_ShouldSaveClienteAndCreateContaBancaria() {
        Cliente cliente = new Cliente();
        cliente.setSenha("password");
        cliente.setNome("John Doe");

        ContaBancaria contaBancaria = new ContaBancaria();
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(contaBancariaService.criarConta(any(Cliente.class))).thenReturn(contaBancaria);

        Cliente result = clienteService.save(cliente);

        assertNotNull(result);
        assertEquals("John Doe", result.getNome());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(contaBancariaService, times(1)).criarConta(any(Cliente.class));
    }

    @Test
    void deleteById_ShouldDeleteCliente_WhenExists() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        clienteService.deleteById(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenNotExists() {
        when(clienteRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> clienteService.deleteById(1L));
        assertEquals("Cliente com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    void update_ShouldUpdateCliente() {
        Cliente existingCliente = new Cliente();
        existingCliente.setId(1L);
        existingCliente.setNome("Old Name");

        Cliente updatedCliente = new Cliente();
        updatedCliente.setNome("New Name");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existingCliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente result = clienteService.update(1L, updatedCliente);

        assertNotNull(result);
        assertEquals("New Name", result.getNome());
        verify(clienteRepository, times(1)).save(existingCliente);
    }
}
