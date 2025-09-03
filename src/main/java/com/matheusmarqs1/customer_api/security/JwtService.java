package com.matheusmarqs1.customer_api.security;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.matheusmarqs1.customer_api.dtos.login.LoginResponse;

@Service
public class JwtService {
	private final  JwtEncoder encoder;
	private final Long expiry = 900L;
	
	public JwtService(JwtEncoder encoder) {
		this.encoder = encoder;
	}
	
	public LoginResponse generateTokenAndResponse(Authentication authentication) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusSeconds(expiry);
		
		CustomerAuthenticated customerAuthenticated = (CustomerAuthenticated) authentication.getPrincipal();
		
		String scopes = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));
		
		var claims = JwtClaimsSet.builder()
				.issuer("customer-api")
				.issuedAt(now)
				.expiresAt(expiresAt)
				.subject(authentication.getName())
				.claim("scope", scopes)
				.claim("customerId", customerAuthenticated.getId())
				.build();
		
		String token = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	    
	    List<String> roles = authentication.getAuthorities().stream()
	        .map(GrantedAuthority::getAuthority)
	        .collect(Collectors.toList());

	    return new LoginResponse(token, roles, expiresAt, customerAuthenticated.getId());
	}
	

}
