package com.matheusmarqs1.customer_api.security;

public record LoginRequest(
		String email,
		String password
) {

}
