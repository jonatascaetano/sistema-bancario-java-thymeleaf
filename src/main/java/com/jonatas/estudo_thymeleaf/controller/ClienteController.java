package com.jonatas.estudo_thymeleaf.controller;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Lista todos os clientes.
     *
     * @return Lista de todos os clientes.
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        List<Cliente> clientes = clienteService.findAll();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id ID do cliente.
     * @return Cliente encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.findById(id);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Cria ou atualiza um cliente.
     *
     * @param cliente Dados do cliente.
     * @return Cliente criado ou atualizado.
     */
    @PostMapping
    public ResponseEntity<Cliente> createOrUpdateCliente(@RequestBody Cliente cliente) {
        Cliente savedCliente = clienteService.save(cliente);
        return new ResponseEntity<>(savedCliente, HttpStatus.CREATED);
    }

    /**
     * Deleta um cliente pelo ID.
     *
     * @param id ID do cliente.
     * @return Status da operação.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.deleteById(id);
            return new ResponseEntity<>("Cliente deletado com sucesso.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Atualiza os dados de um cliente.
     *
     * @param id      ID do cliente a ser atualizado.
     * @param cliente Dados atualizados do cliente.
     * @return Cliente atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            Cliente updatedCliente = clienteService.update(id, cliente);
            return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
