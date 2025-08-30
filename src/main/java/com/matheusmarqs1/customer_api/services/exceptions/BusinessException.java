package com.matheusmarqs1.customer_api.services.exceptions;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public BusinessException(String message) {
		super(message);
	}

}
