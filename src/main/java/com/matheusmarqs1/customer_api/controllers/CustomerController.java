package com.matheusmarqs1.customer_api.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matheusmarqs1.customer_api.dtos.customer.CustomerCreateRequest;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerResponse;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerUpdateRequest;
import com.matheusmarqs1.customer_api.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
	
	private final CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@GetMapping
	public ResponseEntity<Page<CustomerResponse>> findAllCustomers(@RequestParam(defaultValue = "0") int page, 
																   @RequestParam(defaultValue = "10") int size){
		Page<CustomerResponse> customers = customerService.findAllCustomers(page, size);
		return ResponseEntity.ok().body(customers);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable Long id){
		CustomerResponse customer = customerService.findCustomerById(id);
		return ResponseEntity.ok().body(customer);
	}
	
	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest createRequest){
		CustomerResponse customer = customerService.createCustomer(createRequest);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(customer.id()).toUri();
		return ResponseEntity.created(uri).body(customer);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest updateRequest){
		CustomerResponse customer = customerService.updateCustomer(id, updateRequest);
		return ResponseEntity.ok().body(customer);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
		customerService.deleteCustomer(id);
		return ResponseEntity.noContent().build();
	}
}
