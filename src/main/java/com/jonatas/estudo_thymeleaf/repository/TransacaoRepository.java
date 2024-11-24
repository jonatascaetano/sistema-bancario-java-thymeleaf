package com.jonatas.estudo_thymeleaf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jonatas.estudo_thymeleaf.model.entities.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    /**
     * Lista as transações em que a conta é origem ou destino.
     *
     * @param contaId ID da conta.
     * @return Lista de transações relacionadas à conta.
     */
    List<Transacao> findByContaOrigem_IdOrContaDestino_Id(Long contaOrigemId, Long contaDestinoId);

}