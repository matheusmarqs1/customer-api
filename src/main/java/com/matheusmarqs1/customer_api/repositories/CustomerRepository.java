package com.matheusmarqs1.customer_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.matheusmarqs1.customer_api.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
	Optional<Customer> findByCpf(String cpf);
	Optional<Customer> findByEmail(String email);
}
