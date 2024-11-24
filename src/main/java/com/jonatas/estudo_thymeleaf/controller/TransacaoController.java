package com.jonatas.estudo_thymeleaf.controller;

import com.jonatas.estudo_thymeleaf.model.entities.Transacao;
import com.jonatas.estudo_thymeleaf.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    /**
     * Realiza uma transação de crédito.
     *
     * @param contaId ID da conta que será creditada.
     * @param valor   Valor do crédito.
     * @return ResponseEntity com a transação registrada.
     */
    @PostMapping("/deposito")
    public ResponseEntity<Transacao> creditar(@RequestParam Long contaId, @RequestParam BigDecimal valor) {
        Transacao transacao = transacaoService.creditar(contaId, valor);
        return ResponseEntity.ok(transacao);
    }

    /**
     * Realiza uma transação de débito.
     *
     * @param contaId ID da conta que será debitada.
     * @param valor   Valor do débito.
     * @return ResponseEntity com a transação registrada.
     */
    @PostMapping("/saque")
    public ResponseEntity<Transacao> debitar(@RequestParam Long contaId, @RequestParam BigDecimal valor) {
        Transacao transacao = transacaoService.debitar(contaId, valor);
        return ResponseEntity.ok(transacao);
    }

    /**
     * Realiza uma transferência entre duas contas bancárias.
     *
     * @param contaOrigemId  ID da conta de origem.
     * @param contaDestinoId ID da conta de destino.
     * @param valor          Valor da transferência.
     * @return ResponseEntity com a transação registrada.
     */
    @PostMapping("/transferencia")
    public ResponseEntity<Transacao> transferir(@RequestParam Long contaOrigemId, @RequestParam Long contaDestinoId,
                                                @RequestParam BigDecimal valor) {
        Transacao transacao = transacaoService.transferir(contaOrigemId, contaDestinoId, valor);
        return ResponseEntity.ok(transacao);
    }

    /**
     * Lista todas as transações registradas.
     *
     * @return ResponseEntity com a lista de transações.
     */
    @GetMapping
    public ResponseEntity<List<Transacao>> listarTransacoes() {
        List<Transacao> transacoes = transacaoService.listarTransacoes();
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Lista as transações de uma conta específica.
     *
     * @param contaId ID da conta.
     * @return ResponseEntity com a lista de transações da conta.
     */
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<Transacao>> listarTransacoesPorConta(@PathVariable Long contaId) {
        List<Transacao> transacoes = transacaoService.listarTransacoesPorConta(contaId);
        return ResponseEntity.ok(transacoes);
    }
}
