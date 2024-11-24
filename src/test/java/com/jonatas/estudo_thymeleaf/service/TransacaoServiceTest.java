package com.jonatas.estudo_thymeleaf.service;

import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.model.entities.Transacao;
import com.jonatas.estudo_thymeleaf.model.enuns.TipoTransacao;
import com.jonatas.estudo_thymeleaf.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ContaBancariaService contaBancariaService;

    @InjectMocks
    private TransacaoService transacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creditar_ShouldRegisterAndReturnTransacao() {
        Long contaId = 1L;
        BigDecimal valor = new BigDecimal("100.00");
        ContaBancaria conta = new ContaBancaria();
        conta.setId(contaId);

        Transacao transacao = new Transacao(null, conta, TipoTransacao.DEPOSITO, valor, LocalDateTime.now());
        when(contaBancariaService.validarContaExistente(contaId)).thenReturn(conta);
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        Transacao result = transacaoService.creditar(contaId, valor);

        assertNotNull(result);
        assertEquals(TipoTransacao.DEPOSITO, result.getTipoTransacao());
        assertEquals(valor, result.getValor());
        verify(contaBancariaService, times(1)).creditar(contaId, valor);
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void debitar_ShouldRegisterAndReturnTransacao() {
        Long contaId = 1L;
        BigDecimal valor = new BigDecimal("50.00");
        ContaBancaria conta = new ContaBancaria();
        conta.setId(contaId);

        Transacao transacao = new Transacao(conta, null, TipoTransacao.SAQUE, valor, LocalDateTime.now());
        when(contaBancariaService.validarContaExistente(contaId)).thenReturn(conta);
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        Transacao result = transacaoService.debitar(contaId, valor);

        assertNotNull(result);
        assertEquals(TipoTransacao.SAQUE, result.getTipoTransacao());
        assertEquals(valor, result.getValor());
        verify(contaBancariaService, times(1)).debitar(contaId, valor);
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void transferir_ShouldRegisterAndReturnTransacao() {
        Long contaOrigemId = 1L;
        Long contaDestinoId = 2L;
        BigDecimal valor = new BigDecimal("30.00");
        ContaBancaria contaOrigem = new ContaBancaria();
        contaOrigem.setId(contaOrigemId);
        ContaBancaria contaDestino = new ContaBancaria();
        contaDestino.setId(contaDestinoId);

        Transacao transacao = new Transacao(contaOrigem, contaDestino, TipoTransacao.TRANSFERENCIA, valor, LocalDateTime.now());
        when(contaBancariaService.validarContaExistente(contaOrigemId)).thenReturn(contaOrigem);
        when(contaBancariaService.validarContaExistente(contaDestinoId)).thenReturn(contaDestino);
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        Transacao result = transacaoService.transferir(contaOrigemId, contaDestinoId, valor);

        assertNotNull(result);
        assertEquals(TipoTransacao.TRANSFERENCIA, result.getTipoTransacao());
        assertEquals(valor, result.getValor());
        verify(contaBancariaService, times(1)).transferir(contaOrigemId, contaDestinoId, valor);
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    void listarTransacoes_ShouldReturnAllTransacoes() {
        Transacao transacao1 = new Transacao();
        Transacao transacao2 = new Transacao();
        when(transacaoRepository.findAll()).thenReturn(Arrays.asList(transacao1, transacao2));

        List<Transacao> result = transacaoService.listarTransacoes();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transacaoRepository, times(1)).findAll();
    }

    @Test
    void listarTransacoesPorConta_ShouldReturnTransacoesForGivenConta() {
        Long contaId = 1L;
        Transacao transacao1 = new Transacao();
        Transacao transacao2 = new Transacao();
        when(transacaoRepository.findByContaOrigem_IdOrContaDestino_Id(contaId, contaId))
                .thenReturn(Arrays.asList(transacao1, transacao2));

        List<Transacao> result = transacaoService.listarTransacoesPorConta(contaId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transacaoRepository, times(1)).findByContaOrigem_IdOrContaDestino_Id(contaId, contaId);
    }
}
