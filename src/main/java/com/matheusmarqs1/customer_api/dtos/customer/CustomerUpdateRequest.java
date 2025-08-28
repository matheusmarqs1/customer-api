package com.matheusmarqs1.customer_api.dtos.customer;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
		@NotBlank(message = "Name is required")
		@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
		String name,
		
		@NotBlank(message = "Email is required")
		@Size(max = 100, message = "Email must not exceed 100 characters")
		@Email(message = "Invalid email format")
		String email,
		
		@NotNull(message = "Date of birth is required")
		@Past(message = "Date of birth must be in the past")
		@JsonFormat(pattern = "yyyy-MM-dd")
		LocalDate birthDate,
		
		@NotBlank(message = "Phone number is required")
		@Pattern(regexp = "^\\d{10,11}$", message = "Phone number must contain 10 or 11 digits")
		String phone
		) {

}
