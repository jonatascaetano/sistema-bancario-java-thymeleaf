-- V2__insert_initial_data.sql

-- Inserir dados iniciais na tabela tb_cliente
INSERT INTO tb_cliente (nome, cpf, email, telefone, endereco, senha, role)
VALUES 
('João Silva', '123.456.789-01', 'joao@email.com', '11987654321', 'Rua A, 123', '$2a$12$Z5yEmewxqJcWG5Wfz/DgZ.uDEm7mHMfsFPPvxa4h4AIVs2r199qZ2', 'USER'),
('Maria Oliveira', '987.654.321-00', 'maria@email.com', '11987654322', 'Rua B, 456', '$2a$12$JybRy/s0P8ay7NRjALT.4.mORTeRIwHSlbQzAA/tMNDFnWlEsJ6WC', 'ADMIN');


-- Inserir dados na tabela tb_contabancaria
INSERT INTO tb_contabancaria (numero_conta, agencia, saldo, tipo_conta, cliente_id)
VALUES 
('12345-6', '001', 1000.00, 'CORRENTE', 1),
('23456-7', '002', 1500.00, 'POUPANCA', 2);

-- Inserir dados com uma data específica
INSERT INTO tb_transacao (tipo_transacao, valor, data_hora, conta_origem_id, conta_destino_id)
VALUES
('DEPOSITO', 200.00, '2024-11-17 12:45:30', NULL, 1),
('SAQUE', 100.00, '2024-11-17 12:45:30', 1, NULL),
('TRANSFERENCIA', 50.00, '2024-11-17 12:45:30', 1, 2);
