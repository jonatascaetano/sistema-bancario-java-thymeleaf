package com.jonatas.estudo_thymeleaf.controller;

import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.service.ContaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contas")
public class ContaBancariaController {

    @Autowired
    private ContaBancariaService contaBancariaService;

    /**
     * Busca uma conta pelo ID.
     *
     * @param id ID da conta.
     * @return Conta encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContaBancaria> buscarConta(@PathVariable Long id) {
        try {
            ContaBancaria conta = contaBancariaService.validarContaExistente(id);
            return new ResponseEntity<>(conta, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
