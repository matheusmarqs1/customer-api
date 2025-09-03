package com.matheusmarqs1.customer_api.dtos.login;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
		@Schema(description = "JWT token to be used for authenticated requests", 
		example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
		String token,
		@Schema(description = "Roles granted to the authenticated user", example = "[\"ROLE_CUSTOMER\"]")
		List<String> roles,
		@Schema(description = "Token expiration time in GMT, formatted as ISO-8601 up to seconds", example = "2025-09-02T15:45:30Z")
		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
		Instant expiresAt,
		Long id
		) {
	
}
