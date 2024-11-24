package com.jonatas.estudo_thymeleaf.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jonatas.estudo_thymeleaf.model.entities.Cliente;
import com.jonatas.estudo_thymeleaf.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AuthorizationService implements UserDetailsService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Cliente> cliente = clienteRepository.findBycpf(username);
		if (!cliente.isPresent()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return cliente.get();
	}

}
