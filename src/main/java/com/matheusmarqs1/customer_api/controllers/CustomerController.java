package com.matheusmarqs1.customer_api.controllers;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {
	
	private final CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@Operation(summary = "Get a paginated list of customers", 
			description = "Retrieve a list of customers filtered optionally by name, cpf, email, birthDate, or phone")
	@SecurityRequirement(name = "Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved the list of customers"),
			@ApiResponse(responseCode = "401", description = "Unauthorized. JWT token is missing or invalid")
	})
	@GetMapping
	public ResponseEntity<Page<CustomerResponse>> findCustomers(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String cpf,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) LocalDate birthDate,
			@RequestParam(required = false) String phone,
			@PageableDefault(page = 0, size = 10) Pageable pageable
			){
				Page<CustomerResponse> customers = customerService.findCustomers(name, cpf, email, birthDate, phone, pageable);
				return ResponseEntity.ok().body(customers);
	}
	
	@Operation(summary = "Find customer by id", description = "Retrieve a customer by ther id")
	@SecurityRequirement(name="Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved a customer"),
			@ApiResponse(responseCode = "401", description = "Unauthorized. JWT token is missing or invalid"),
			@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	
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
