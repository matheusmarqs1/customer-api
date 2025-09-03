package com.matheusmarqs1.customer_api.security;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusmarqs1.customer_api.controllers.exceptions.StandardError;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomBearerTokenAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	public CustomBearerTokenAccessDeniedHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		StandardError err = new StandardError(
				Instant.now(), 
				HttpStatus.FORBIDDEN.value(), 
				"Access Denied", 
				"You do not have permission to access this resource",
				request.getRequestURI());
		System.out.println("Objeto StandardError antes da serialização: " + err);
		response.getWriter().write(objectMapper.writeValueAsString(err));
	}

}
