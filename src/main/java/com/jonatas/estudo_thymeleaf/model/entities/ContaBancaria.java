package com.jonatas.estudo_thymeleaf.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jonatas.estudo_thymeleaf.model.enuns.TipoConta;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_contabancaria")
public class ContaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "O número da conta é obrigatório")
    @Size(max = 20, message = "O número da conta deve ter no máximo 20 caracteres")
    @Column(unique = true)
    private String numeroConta;

    @NotBlank(message = "A agência é obrigatória")
    @Size(max = 10, message = "A agência deve ter no máximo 10 caracteres")
    private String agencia;

    @NotNull(message = "O saldo é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "O saldo não pode ser negativo")
    @Column(precision = 15, scale = 2)
    private BigDecimal saldo;

    @NotNull(message = "O tipo de conta é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonBackReference(value = "cliente-conta")
    private Cliente cliente;

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "transacoes-origem")
    private List<Transacao> transacoesOrigem = new ArrayList<>();
    
    @OneToMany(mappedBy = "contaDestino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "transacoes-destino")
    private List<Transacao> transacoesDestino = new ArrayList<>();

    @Version
    private Long version; // Campo de controle de versão
    
    public ContaBancaria() {
    }

    public ContaBancaria(String numeroConta, String agencia, BigDecimal saldo, TipoConta tipoConta, Cliente cliente) {
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @JsonIgnore
    @Transient
    public List<Transacao> getTodasTransacoes() {
        List<Transacao> todasTransacoes = new ArrayList<>();
        todasTransacoes.addAll(transacoesOrigem);
        todasTransacoes.addAll(transacoesDestino);
        return todasTransacoes;
    }

    public List<Transacao> getTransacoesOrigem() {
        return transacoesOrigem;
    }

    public List<Transacao> getTransacoesDestino() {
        return transacoesDestino;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContaBancaria other = (ContaBancaria) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ContaBancaria [id=" + id + ", numeroConta=" + numeroConta + ", agencia=" + agencia + ", saldo=" + saldo
                + ", tipoConta=" + tipoConta + ", cliente=" + cliente + ", version=" + version + "]";
    }

    
}
