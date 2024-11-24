package com.jonatas.estudo_thymeleaf.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jonatas.estudo_thymeleaf.model.entities.ContaBancaria;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long>{

    Optional<ContaBancaria> findByNumeroConta(String numeroConta);
}
