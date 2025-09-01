package com.matheusmarqs1.customer_api.security;

public record AuthRequest(
		String email,
		String password
) {

}
