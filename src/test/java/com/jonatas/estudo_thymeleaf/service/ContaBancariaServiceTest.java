package com.jonatas.estudo_thymeleaf.service;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.model.enuns.TipoConta;
import com.jonatas.estudo_thymeleaf.repository.ContaBancariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaBancariaServiceTest {

    @Mock
    private ContaBancariaRepository contaBancariaRepository;

    @InjectMocks
    private ContaBancariaService contaBancariaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarConta_ShouldCreateAndSaveContaBancaria() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        ContaBancaria contaBancaria = new ContaBancaria(
                "12345-6", "123", BigDecimal.ZERO, TipoConta.CORRENTE, cliente
        );
        when(contaBancariaRepository.save(any(ContaBancaria.class))).thenReturn(contaBancaria);

        ContaBancaria result = contaBancariaService.criarConta(cliente);

        assertNotNull(result);
        assertEquals("12345-6", result.getNumeroConta());
        assertEquals("123", result.getAgencia());
        assertEquals(BigDecimal.ZERO, result.getSaldo());
        verify(contaBancariaRepository, times(1)).save(any(ContaBancaria.class));
    }

    @Test
    void creditar_ShouldIncreaseSaldo() {
        ContaBancaria conta = new ContaBancaria();
        conta.setId(1L);
        conta.setSaldo(new BigDecimal("100.00"));
        when(contaBancariaRepository.findById(1L)).thenReturn(Optional.of(conta));

        contaBancariaService.creditar(1L, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), conta.getSaldo());
        verify(contaBancariaRepository, times(1)).save(conta);
    }

    @Test
    void debitar_ShouldDecreaseSaldo() {
        ContaBancaria conta = new ContaBancaria();
        conta.setId(1L);
        conta.setSaldo(new BigDecimal("100.00"));
        when(contaBancariaRepository.findById(1L)).thenReturn(Optional.of(conta));

        contaBancariaService.debitar(1L, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("50.00"), conta.getSaldo());
        verify(contaBancariaRepository, times(1)).save(conta);
    }

    @Test
    void debitar_ShouldThrowException_WhenSaldoInsuficiente() {
        ContaBancaria conta = new ContaBancaria();
        conta.setId(1L);
        conta.setSaldo(new BigDecimal("30.00"));
        when(contaBancariaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                contaBancariaService.debitar(1L, new BigDecimal("50.00")));

        assertEquals("Saldo insuficiente para debitar o valor solicitado.", exception.getMessage());
    }

    @Test
    void transferir_ShouldTransferSaldoBetweenAccounts() {
        ContaBancaria contaOrigem = new ContaBancaria();
        contaOrigem.setId(1L);
        contaOrigem.setSaldo(new BigDecimal("100.00"));

        ContaBancaria contaDestino = new ContaBancaria();
        contaDestino.setId(2L);
        contaDestino.setSaldo(new BigDecimal("50.00"));

        when(contaBancariaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaBancariaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        contaBancariaService.transferir(1L, 2L, new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), contaOrigem.getSaldo());
        assertEquals(new BigDecimal("80.00"), contaDestino.getSaldo());
        verify(contaBancariaRepository, times(1)).save(contaOrigem);
        verify(contaBancariaRepository, times(1)).save(contaDestino);
    }

    @Test
    void transferir_ShouldThrowException_WhenSaldoInsuficiente() {
        ContaBancaria contaOrigem = new ContaBancaria();
        contaOrigem.setId(1L);
        contaOrigem.setSaldo(new BigDecimal("20.00"));

        ContaBancaria contaDestino = new ContaBancaria();
        contaDestino.setId(2L);
        contaDestino.setSaldo(new BigDecimal("50.00"));

        when(contaBancariaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaBancariaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                contaBancariaService.transferir(1L, 2L, new BigDecimal("30.00")));

        assertEquals("Saldo insuficiente na conta de origem.", exception.getMessage());
    }

    @Test
    void validarContaExistente_ShouldReturnConta_WhenExists() {
        ContaBancaria conta = new ContaBancaria();
        conta.setId(1L);
        when(contaBancariaRepository.findById(1L)).thenReturn(Optional.of(conta));

        ContaBancaria result = contaBancariaService.validarContaExistente(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void validarContaExistente_ShouldThrowException_WhenNotExists() {
        when(contaBancariaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                contaBancariaService.validarContaExistente(1L));

        assertEquals("Conta com ID 1 não encontrada.", exception.getMessage());
    }
}
