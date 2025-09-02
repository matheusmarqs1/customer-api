package com.matheusmarqs1.customer_api.controllers.docs;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.matheusmarqs1.customer_api.dtos.customer.CustomerCreateRequest;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerResponse;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Customer Controller", description = "Endpoints for customer management")
public interface CustomerControllerDocs {
	
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
	);
	
	@Operation(summary = "Find customer by id", description = "Retrieve a customer by their id")
	@SecurityRequirement(name="Bearer Authentication")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved a customer"),
			@ApiResponse(responseCode = "400", description = "Invalid argument type"),
			@ApiResponse(responseCode = "401", description = "Unauthorized. JWT token is missing or invalid"),
			@ApiResponse(responseCode = "404", description = "Customer not found")
	})
	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerResponse> findCustomerById(@PathVariable Long id);
	
	@Operation(summary = "Create a new customer", description = "Register a new customer in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer successfully created"),
        @ApiResponse(responseCode = "400", 
        	description = "Bad request. Can occur due to validation errors "
        			+ "(e.g., invalid email, empty name) or business rules "
        			+ "(e.g., CPF already exists, Email already exists)"),
    })
	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest createRequest);
	
	 @Operation(summary = "Update an existing customer", description = "Update an existing customer in the system")
	 @SecurityRequirement(name="Bearer Authentication")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "200", description = "Customer successfully updated"),
	        @ApiResponse(responseCode = "400", description = "Bad request. "
	        		+ "Can occur due to validation errors (invalid email, phone, password), "
	        		+ "business rule violations (email already exists)"
	        		+ "or invalid argument types (e.g., non-numeric id in path)"),
	        @ApiResponse(responseCode = "401", description = "Unauthorized. JWT token is missing or invalid"),
	        @ApiResponse(responseCode = "404", description = "Customer not found")
	    })
	 @PutMapping(value = "/{id}")
		public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequest updateRequest);
	 
	 @Operation(summary = "Delete a customer", description = "Remove a customer from the system by id")
	 @SecurityRequirement(name="Bearer Authentication")
	    @ApiResponses(value = {
	        @ApiResponse(responseCode = "204", description = "Customer successfully deleted"),
	        @ApiResponse(responseCode = "400", description = "Bad request. Can occur if the ID parameter is invalid (e.g., not a number)"),
	        @ApiResponse(responseCode = "401", description = "Unauthorized. JWT token is missing or invalid"),
	        @ApiResponse(responseCode = "404", description = "Customer not found")
	    })
	 @DeleteMapping(value = "/{id}")
		public ResponseEntity<Void> deleteCustomer(@PathVariable Long id);

}
