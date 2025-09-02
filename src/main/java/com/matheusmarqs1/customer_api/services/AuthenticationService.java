package com.matheusmarqs1.customer_api.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.matheusmarqs1.customer_api.dtos.login.LoginRequest;
import com.matheusmarqs1.customer_api.dtos.login.LoginResponse;
import com.matheusmarqs1.customer_api.security.JwtService;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	public AuthenticationService(JwtService jwtService, AuthenticationManager authenticationManager) {
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}
	
	public LoginResponse authenticate(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
		
		String token = jwtService.generateToken(authentication);
		
		List<String> roles = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		Instant expiresAt = jwtService.getExpirationTime();
		
		return new LoginResponse(token, roles, expiresAt);
	}

}
