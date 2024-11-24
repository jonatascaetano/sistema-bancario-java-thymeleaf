package com.jonatas.estudo_thymeleaf.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;
import com.jonatas.estudo_thymeleaf.model.entities.Transacao;
import com.jonatas.estudo_thymeleaf.service.ClienteService;
import com.jonatas.estudo_thymeleaf.service.ContaBancariaService;
import com.jonatas.estudo_thymeleaf.service.TransacaoService;

@Controller
public class ProfileController {

    @Autowired
    ClienteService clienteService;

    @Autowired
    ContaBancariaService contaBancariaService;

    @Autowired
    TransacaoService transacaoService;

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Cliente cliente = clienteService.findBycpf(userDetails.getUsername());
        ContaBancaria contaBancaria = contaBancariaService.validarContaExistente(cliente.getContaBancaria().getId());
        List<Transacao> transacoes = transacaoService.listarTransacoesPorConta(contaBancaria.getId());
        model.addAttribute("cliente", cliente);
        model.addAttribute("contaBancaria", contaBancaria);
        model.addAttribute("transacoes", transacoes);
        return "profile";
    }
}
