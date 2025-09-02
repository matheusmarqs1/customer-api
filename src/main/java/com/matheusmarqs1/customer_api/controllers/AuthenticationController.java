package com.matheusmarqs1.customer_api.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusmarqs1.customer_api.security.AuthRequest;
import com.matheusmarqs1.customer_api.services.AuthenticationService;

@RestController
@RequestMapping("/customers")
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/login")
	public String authenticate(@RequestBody AuthRequest request) {
		return authenticationService.authenticate(request.email(), request.password());
	}

}
