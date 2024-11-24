package com.jonatas.estudo_thymeleaf.view;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.service.ContaBancariaService;
import com.jonatas.estudo_thymeleaf.service.TransacaoService;

@Controller
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private ContaBancariaService contaBancariaService;
    @PostMapping("/creditar")
    public String creditar(@RequestParam Long contaId, @RequestParam BigDecimal valor, RedirectAttributes redirectAttributes) {
        try {
            transacaoService.creditar(contaId, valor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Depósito realizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            redirectAttributes.addFlashAttribute("modalErro", "depositar");
        }
        return "redirect:/profile";
    }
    
    @PostMapping("/debitar")
    public String debitar(@RequestParam Long contaId, @RequestParam BigDecimal valor, RedirectAttributes redirectAttributes) {
        try {
            transacaoService.debitar(contaId, valor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Saque realizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            redirectAttributes.addFlashAttribute("modalErro", "retirar");
        }
        return "redirect:/profile";
    }
    
    @PostMapping("/transferir")
    public String transferir(@RequestParam Long contaOrigemId, @RequestParam String numeroContaDestino, @RequestParam BigDecimal valor, RedirectAttributes redirectAttributes) {
        try {
            ContaBancaria contaDestino = contaBancariaService.findByNumeroConta(numeroContaDestino);
            transacaoService.transferir(contaOrigemId, contaDestino.getId(), valor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Transferência realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            redirectAttributes.addFlashAttribute("modalErro", "transferir");
        }
        return "redirect:/profile";
    }
    
}
