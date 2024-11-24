package com.jonatas.estudo_thymeleaf.service;

import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.model.entities.Transacao;
import com.jonatas.estudo_thymeleaf.model.enuns.TipoTransacao;
import com.jonatas.estudo_thymeleaf.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaBancariaService contaBancariaService;

    /**
     * Realiza uma transação de crédito.
     *
     * @param contaId ID da conta que será creditada.
     * @param valor   Valor do crédito.
     * @return Transação registrada.
     */
    @Transactional
    public Transacao creditar(Long contaId, BigDecimal valor) {
        contaBancariaService.creditar(contaId, valor);
        return registrarTransacao(null, contaId, valor, TipoTransacao.DEPOSITO);
    }

    /**
     * Realiza uma transação de débito.
     *
     * @param contaId ID da conta que será debitada.
     * @param valor   Valor do débito.
     * @return Transação registrada.
     */
    @Transactional
    public Transacao debitar(Long contaId, BigDecimal valor) {
        contaBancariaService.debitar(contaId, valor);
        return registrarTransacao(contaId, null, valor, TipoTransacao.SAQUE);
    }

    /**
     * Realiza uma transferência entre duas contas bancárias.
     *
     * @param contaOrigemId  ID da conta de origem.
     * @param contaDestinoId ID da conta de destino.
     * @param valor          Valor da transferência.
     * @return Transação registrada.
     */
    @Transactional
    public Transacao transferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        contaBancariaService.transferir(contaOrigemId, contaDestinoId, valor);
        return registrarTransacao(contaOrigemId, contaDestinoId, valor, TipoTransacao.TRANSFERENCIA);
    }

    /**
     * Lista todas as transações registradas.
     *
     * @return Lista de transações.
     */
    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }

    /**
     * Lista as transações de uma conta específica.
     *
     * @param contaId ID da conta.
     * @return Lista de transações relacionadas à conta.
     */
    public List<Transacao> listarTransacoesPorConta(Long contaId) {
        List<Transacao> transacoes = transacaoRepository.findByContaOrigem_IdOrContaDestino_Id(contaId, contaId);
        return transacoes;
    }

    /**
     * Registra uma transação no banco de dados.
     *
     * @param contaOrigemId  ID da conta de origem (ou null para crédito).
     * @param contaDestinoId ID da conta de destino (ou null para débito).
     * @param valor          Valor da transação.
     * @param tipo           Tipo da transação.
     * @return Transação registrada.
     */
    private Transacao registrarTransacao(Long contaOrigemId, Long contaDestinoId, BigDecimal valor,
            TipoTransacao tipo) {
        ContaBancaria contaOrigem = contaOrigemId != null ? contaBancariaService.validarContaExistente(contaOrigemId)
                : null;
        ContaBancaria contaDestino = contaDestinoId != null ? contaBancariaService.validarContaExistente(contaDestinoId)
                : null;

        // Garantir que a contaOrigem e contaDestino não sejam ambos nulos, se
        // necessário
        if (contaOrigem == null && contaDestino == null) {
            throw new IllegalArgumentException("Pelo menos uma conta deve ser fornecida.");
        }

        Transacao transacao = new Transacao(contaOrigem, contaDestino, tipo, valor, LocalDateTime.now());
        return transacaoRepository.save(transacao);
    }

}
