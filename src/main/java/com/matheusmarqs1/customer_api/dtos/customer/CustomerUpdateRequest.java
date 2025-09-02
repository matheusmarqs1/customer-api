package com.matheusmarqs1.customer_api.dtos.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
		@Schema(description = "Full name of the customer", example = "João Pedro")
		@NotBlank(message = "Name is required")
		@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
		String name,
		
		@Schema(description = "Email address of the customer", example = "joaopedro@example.com")
		@NotBlank(message = "Email is required")
		@Size(max = 100, message = "Email must not exceed 100 characters")
		@Email(message = "Invalid email format")
		String email,
		
		@Schema(description = "Birth date in yyyy-MM-dd format", example = "2001-08-06")
		@NotNull(message = "Date of birth is required")
		@Past(message = "Date of birth must be in the past")
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate birthDate,
		
		@Schema(description = "Phone number with only digits, must be between 10 and 11 digits", example = "11999999999")
		@NotBlank(message = "Phone number is required")
		@Size(min = 10, max = 11, message = "Phone number must be between 10 and 11 digits")
		@Pattern(regexp = "^\\d+$", message = "Phone number must contain only digits")
		String phone,
		
		@Schema(description = "Password with complexity requirements", example = "AlkPQ12@")
		@NotBlank(message = "Password is required")
		@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
		@Pattern(
				regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W)[A-Za-z\\d\\W_]+$", 
				message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
		String password
		) {

}
