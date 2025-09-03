package com.matheusmarqs1.customer_api.entities.enums;

public enum Role {
	
	ROLE_CUSTOMER(1),
	ROLE_ADMIN(2);
	
	
	private int code;
	
	private Role(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static Role valueOf(int code) {
		for(Role role : Role.values()) {
			if(role.getCode() == code) {
				return role;
			}
		}
		throw new IllegalArgumentException("Invalid Role");
	}

			
}
