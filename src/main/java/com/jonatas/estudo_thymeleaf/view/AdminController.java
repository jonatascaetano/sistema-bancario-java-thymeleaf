package com.jonatas.estudo_thymeleaf.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.service.ClienteService;

@Controller
public class AdminController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/admin")
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Cliente admin = clienteService.findBycpf(userDetails.getUsername());
        List<Cliente> clientes = clienteService.findAll();
        model.addAttribute("clientes", clientes);
        model.addAttribute("admin", admin);
        // Define um cliente selecionado inicial (opcional)
        model.addAttribute("clienteSelecionado", new Cliente());
        return "admin";
    }

    @PostMapping("/admin/atualizarCliente")
    public String atualizarCliente(@AuthenticationPrincipal UserDetails userDetails,  @RequestParam Long id, @ModelAttribute Cliente cliente, BindingResult bindingResult,
            Model model) {
                Cliente admin = clienteService.findBycpf(userDetails.getUsername());
                model.addAttribute("admin", admin);

        // Associando explicitamente o id ao cliente
        cliente.setId(id);
        try {
            clienteService.update(id, cliente); // Atualiza o cliente no banco de dados
            return "redirect:/admin";

        } catch (Exception e) {
            model.addAttribute("modalErro", "editar");
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("cliente", cliente); // Manter os dados preenchidos no formulário

            return "admin"; // Se ocorrer algum erro durante a atualização
        }
    }

    @PostMapping("/admin/novoCliente")
    public String salvarCliente(@AuthenticationPrincipal UserDetails userDetails,  @ModelAttribute Cliente cliente, Model model, RedirectAttributes redirectAttributes) {
        Cliente admin = clienteService.findBycpf(userDetails.getUsername());
        model.addAttribute("admin", admin);

        try {
            System.out.println("Cliente recebido: " + cliente.toString());
            clienteService.save(cliente);
            redirectAttributes.addFlashAttribute("clientes", clienteService.findAll());
            return "redirect:/admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("modalErro", "criar");
            redirectAttributes.addFlashAttribute("clientes", clienteService.findAll());
            redirectAttributes.addFlashAttribute("clienteNovo", cliente);
            return "redirect:/admin";
        }
    }

}
