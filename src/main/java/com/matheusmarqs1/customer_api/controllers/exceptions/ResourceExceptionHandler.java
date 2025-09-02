package com.matheusmarqs1.customer_api.controllers.exceptions;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.matheusmarqs1.customer_api.services.exceptions.BusinessException;
import com.matheusmarqs1.customer_api.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request){
		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<StandardError> handleBusinessException(BusinessException e, HttpServletRequest request){
		String error = "Business rule violation";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<StandardError> handleDateTimeParseException(DateTimeParseException e, HttpServletRequest request){
		String error = "Invalid date format";
		String message = "Invalid date format. Expected format: yyyy-MM-dd";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request){
		
		String message = e.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining("; "));
		
		String error = "Validation failed";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
		
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<StandardError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request){
		 String error = "Invalid argument type";
		 HttpStatus status = HttpStatus.BAD_REQUEST;
		 String message = String.format("Parameter '%s' with value '%s' is invalid. It must be of type %s", 
                 e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
		 
		 StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
	     return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<StandardError> handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request){
		String error = "Customer not found";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<StandardError> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request){
		String error = "Invalid credentials";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String message = "Email or password is incorrect";
		StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(InvalidBearerTokenException.class)
	public ResponseEntity<StandardError> handleInvalidBearerException(InvalidBearerTokenException e, HttpServletRequest request){
		String error = "Invalid Token";
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		String message = "The access token provided is expired, revoked, malformed, or invalid";
		StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> handleOtherException(Exception e, HttpServletRequest request){
		String error = "Internal Server Error";
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = "A server internal error occurs";
		StandardError err = new StandardError(Instant.now(), status.value(), error, message, request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
}
