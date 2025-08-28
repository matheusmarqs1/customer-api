package com.matheusmarqs1.customer_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheusmarqs1.customer_api.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByCpf(String cpf);
	Optional<Customer> findByEmail(String email);
}
