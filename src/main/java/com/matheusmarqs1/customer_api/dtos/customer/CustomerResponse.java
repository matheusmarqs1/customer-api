package com.matheusmarqs1.customer_api.dtos.customer;

import java.time.LocalDate;

import com.matheusmarqs1.customer_api.entities.Customer;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerResponse(
		@Schema(description = "Unique identifier for the customer", example = "1")
		Long id,
		@Schema(description = "Full name of the customer", example = "João Pedro")
		String name, 
		@Schema(description = "CPF of the customer", example = "12345678901")
		String cpf,
		@Schema(description = "Email of the customer", example = "joaopedro@example.com")
		String email,
		@Schema(description = "Customer's date of birth in the format yyyy-MM-dd", example = "2001-08-06")
		LocalDate birthDate,
		@Schema(description = "Phone number of the customer", example = "11999999999")
		String phone,
		@Schema(description = "Customer age", example = "21")
		Integer age
		) {
	
			public static CustomerResponse fromEntity(Customer customer) {
				return new CustomerResponse(customer.getId(), 
						customer.getName(), 
						customer.getCpf(), 
						customer.getEmail(), 
						customer.getBirthDate(), 
						customer.getPhone(), 
						customer.getAge()
				);
			}	

}
