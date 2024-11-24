package com.jonatas.estudo_thymeleaf.service;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaBancariaService contaBancariaService;

    /**
     * Lista todos os clientes.
     *
     * @return Lista de todos os clientes cadastrados.
     */
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    /**
     * Busca um cliente por ID.
     *
     * @param id ID do cliente.
     * @return Cliente encontrado ou exceção se não existir.
     */
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com ID " + id + " não encontrado."));
    }

    public Cliente findBycpf(String cpf) {
        return clienteRepository.findBycpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente com cpf " + cpf + " não encontrado."));
    }

    /**
     * Cria ou atualiza um cliente.
     *
     * @param cliente Dados do cliente.
     * @return Cliente salvo.
     */
    public Cliente save(Cliente cliente) {
        try {
            // Criptografa a senha antes de definir
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Cliente clienteNovo = new Cliente();
            clienteNovo.setNome(cliente.getNome());
            clienteNovo.setCpf(cliente.getCpf());
            clienteNovo.setEmail(cliente.getEmail());
            clienteNovo.setTelefone(cliente.getTelefone());
            clienteNovo.setEndereco(cliente.getEndereco());
            clienteNovo.setRole(cliente.getRole());
            clienteNovo.setSenha(encoder.encode(cliente.getSenha()));

            clienteNovo = clienteRepository.save(clienteNovo);
            ContaBancaria contaBancaria = contaBancariaService.criarConta(clienteNovo);
            cliente.setContaBancaria(contaBancaria);
            return cliente;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException("Erro ao salvar o cliente: " + e.getMessage());
        }
    }

    /**
     * Deleta um cliente pelo ID.
     *
     * @param id ID do cliente.
     */
    public void deleteById(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    /**
     * Atualiza os dados de um cliente.
     *
     * @param id      ID do cliente a ser atualizado.
     * @param cliente Dados atualizados do cliente.
     * @return Cliente atualizado.
     */
    public Cliente update(Long id, Cliente cliente) {
        try {
            Cliente existingCliente = findById(id);
            existingCliente.setNome(cliente.getNome());
            existingCliente.setCpf(cliente.getCpf());
            existingCliente.setEmail(cliente.getEmail());
            existingCliente.setEndereco(cliente.getEndereco());
            existingCliente.setTelefone(cliente.getTelefone());
            existingCliente.setRole(cliente.getRole());
            return clienteRepository.save(existingCliente);

        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar o cliente: " + e.getMessage());
        }
    }
}
