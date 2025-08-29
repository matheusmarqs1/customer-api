package com.matheusmarqs1.customer_api.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusmarqs1.customer_api.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Page<Customer> findAll(Pageable pageable);
	Optional<Customer> findByCpf(String cpf);
	Optional<Customer> findByEmail(String email);
}
