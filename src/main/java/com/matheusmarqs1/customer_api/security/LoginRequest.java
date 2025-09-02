package com.matheusmarqs1.customer_api.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
		
		@Schema(description = "Customer's email for login, must be a valid email format", example = "ana@example.com")
		@NotBlank(message = "Email is required")
		@Size(max = 100, message = "Email must not exceed 100 characters")
		@Email(message = "Invalid email format")
		String email,
		
		@Schema(description = "Customer's password, 8-20 characters with at least one uppercase, one lowercase, one number, and one special character", 
		        example = "AlkPQ12@")
		@NotBlank(message = "Password is required")
		@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
		@Pattern(
				regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W)[A-Za-z\\d\\W_]+$", 
				message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
		String password
) {

}
