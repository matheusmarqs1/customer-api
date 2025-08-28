package com.matheusmarqs1.customer_api.dtos.customer;

import java.time.LocalDate;

import com.matheusmarqs1.customer_api.entities.Customer;

public record CustomerResponse(
		Long id,
		String name, 
		String cpf,
		String email,
		LocalDate birthDate,
		String phone,
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
