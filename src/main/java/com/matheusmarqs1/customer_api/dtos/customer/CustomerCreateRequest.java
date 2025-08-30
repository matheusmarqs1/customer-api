package com.matheusmarqs1.customer_api.dtos.customer;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
		@NotBlank(message = "Name is required")
		@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
		String name,
		
		@NotBlank(message = "CPF is required")
		@Size(min = 11, max = 11, message = "CPF must contain 11 digits")
		@CPF(message = "Invalid CPF")
		String cpf,
		
		@NotBlank(message = "Email is required")
		@Size(max = 100, message = "Email must not exceed 100 characters")
		@Email(message = "Invalid email format")
		String email,
		
		@NotNull(message = "Date of birth is required")
		@Past(message = "Date of birth must be in the past")
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate birthDate,
		
		@NotBlank(message = "Phone number is required")
		@Size(min = 10, max = 11, message = "Phone number must be between 10 and 11 digits")
		@Pattern(regexp = "^\\d+$", message = "Phone number must contain only digits")
		String phone,
		
		@NotBlank(message = "Password is required")
		@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
		@Pattern(
				regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W)[A-Za-z\\d\\W_]+$", 
				message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
		String password
		) {

}
