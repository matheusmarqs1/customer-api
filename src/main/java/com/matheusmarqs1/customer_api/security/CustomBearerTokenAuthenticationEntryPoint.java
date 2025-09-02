package com.matheusmarqs1.customer_api.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomBearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	private final ObjectMapper objectMapper;
	
	public CustomBearerTokenAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		response.setHeader("WWW-Authenticate", "Bearer realm=\"realm\"");
		
		var errorDetails = new ErrorDetails(
				Instant.now().toString(),
				HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized",
				authException.getMessage(),
				request.getRequestURI()
		);
		
		response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
	}
	
	
	private record ErrorDetails(
		String timestamp,
		Integer status,
		String error,
		String message,
		String path) {}

}
