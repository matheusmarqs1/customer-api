package com.matheusmarqs1.customer_api.controllers.docs;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.matheusmarqs1.customer_api.security.LoginRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Controller", description = "Endpoints for authentication")
public interface AuthenticationControllerDocs {
	 @Operation(summary = "Authenticate a customer",
		        description = "Authenticate a customer using email and password and return a JWT token"
	 )
	 @ApiResponses(value = {
			    @ApiResponse(responseCode = "200", description = "Successfully authenticated. Returns JWT token"),
			    @ApiResponse(responseCode = "400", description = "Bad request due to validation errors (e.g., invalid email or password constraints)"),
			    @ApiResponse(responseCode = "401", description = "Unauthorized. Email not found or password incorrect")
	})
	@PostMapping("/login")
	public String authenticate(@RequestBody LoginRequest request);

}
