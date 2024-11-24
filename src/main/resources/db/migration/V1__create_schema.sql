-- V1__create_schema.sql

-- Tabela Clientes
CREATE TABLE tb_cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(150),
    telefone VARCHAR(15),
    endereco VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL
);

-- Tabela Conta Bancária
CREATE TABLE tb_contabancaria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_conta VARCHAR(20) NOT NULL UNIQUE,
    agencia VARCHAR(10) NOT NULL,
    saldo DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    version BIGINT NOT NULL DEFAULT 0,
    tipo_conta VARCHAR(50) NOT NULL,
    cliente_id BIGINT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES tb_cliente(id)
);

-- Tabela Transações
CREATE TABLE tb_transacao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_transacao VARCHAR(50) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    conta_origem_id BIGINT,
    conta_destino_id BIGINT,
    FOREIGN KEY (conta_origem_id) REFERENCES tb_contabancaria(id),
    FOREIGN KEY (conta_destino_id) REFERENCES tb_contabancaria(id)
);
