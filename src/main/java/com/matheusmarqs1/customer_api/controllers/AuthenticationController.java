package com.matheusmarqs1.customer_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusmarqs1.customer_api.controllers.docs.AuthenticationControllerDocs;
import com.matheusmarqs1.customer_api.dtos.login.LoginRequest;
import com.matheusmarqs1.customer_api.dtos.login.LoginResponse;
import com.matheusmarqs1.customer_api.services.AuthenticationService;

@RestController
@RequestMapping("/customers")
public class AuthenticationController implements AuthenticationControllerDocs {
	
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
		LoginResponse response = authenticationService.authenticate(request);
		return ResponseEntity.ok().body(response);
	}

}
