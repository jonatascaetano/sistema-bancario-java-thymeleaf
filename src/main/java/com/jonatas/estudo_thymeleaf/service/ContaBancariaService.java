package com.jonatas.estudo_thymeleaf.service;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.model.enuns.TipoConta;
import com.jonatas.estudo_thymeleaf.repository.ContaBancariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class ContaBancariaService {

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    /**
     * Cria uma nova conta bancária.
     *
     * @param contaBancaria Dados da nova conta.
     * @return Conta criada.
     */
    public ContaBancaria criarConta(Cliente cliente) {
        ContaBancaria contaBancaria = new ContaBancaria(gerarNumeroConta(), gerarNumeroAgencia(), new BigDecimal(0.0),
                TipoConta.CORRENTE, cliente);
        return contaBancariaRepository.save(contaBancaria);
    }

    /**
     * Credita um valor na conta bancária.
     *
     * @param contaId ID da conta a ser creditada.
     * @param valor   Valor a ser creditado.
     */
    @Transactional
    public void creditar(Long contaId, BigDecimal valor) {
        ContaBancaria conta = validarContaExistente(contaId);
        conta.setSaldo(conta.getSaldo().add(valor));
        contaBancariaRepository.save(conta);
    }

    /**
     * Debita um valor da conta bancária.
     *
     * @param contaId ID da conta a ser debitada.
     * @param valor   Valor a ser debitado.
     */
    @Transactional
    public void debitar(Long contaId, BigDecimal valor) {
        ContaBancaria conta = validarContaExistente(contaId);
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para debitar o valor solicitado.");
        }
        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaBancariaRepository.save(conta);
    }

    /**
     * Transfere valor entre duas contas bancárias.
     *
     * @param contaOrigemId  ID da conta de origem.
     * @param contaDestinoId ID da conta de destino.
     * @param valor          Valor a ser transferido.
     */
    @Transactional
    public void transferir(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        if (contaOrigemId.equals(contaDestinoId)) {
            throw new IllegalArgumentException("A conta de origem não pode ser igual à conta de destino.");
        }

        ContaBancaria contaOrigem = validarContaExistente(contaOrigemId);
        ContaBancaria contaDestino = validarContaExistente(contaDestinoId);

        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na conta de origem.");
        }

        // Debita da conta de origem
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaBancariaRepository.save(contaOrigem);

        // Credita na conta de destino
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));
        contaBancariaRepository.save(contaDestino);
    }

    /**
     * Busca uma conta pelo ID e valida sua existência.
     *
     * @param contaId ID da conta.
     * @return Conta bancária encontrada.
     */
    public ContaBancaria validarContaExistente(Long contaId) {
        return contaBancariaRepository.findById(contaId)
                .orElseThrow(() -> new IllegalArgumentException("Conta com ID " + contaId + " não encontrada."));
    }

    public ContaBancaria findByNumeroConta(String numeroConta) {
        return contaBancariaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(
                        () -> new IllegalArgumentException("Conta com numero " + numeroConta + " não encontrada."));
    }

    // Método para gerar o número da conta
    public static String gerarNumeroConta() {
        Random random = new Random();

        // Gera os 5 primeiros dígitos
        StringBuilder numeroConta = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            numeroConta.append(random.nextInt(10)); // Adiciona dígitos de 0 a 9
        }

        // Gera o dígito verificador (último dígito)
        int digitoVerificador = random.nextInt(10);

        String numeroFinal = numeroConta.toString() + "-" + digitoVerificador;

        // Retorna no formato xxxx-x
        return numeroFinal;
    }

    // Método para gerar o número da agência
    public static String gerarNumeroAgencia() {
        Random random = new Random();
        // Gera um número aleatório de exatamente 3 dígitos
        StringBuilder agencia = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            agencia.append(random.nextInt(10)); // Adiciona dígitos de 0 a 9
        }
        return agencia.toString();
    }
}
