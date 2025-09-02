package com.matheusmarqs1.customer_api.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.matheusmarqs1.customer_api.repositories.CustomerRepository;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {
	
	private final CustomerRepository customerRepository;
	
	public CustomerDetailsServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	/**
     * This method is required by Spring Security.
     * The 'username' parameter is used as a generic name for the user's unique identifier,
     * which in this case is the customer's email.
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return customerRepository.findByEmail(username)
				.map(CustomerAuthenticated::new)
				.orElseThrow(() -> new UsernameNotFoundException("Customer not found with the provided email"));
	}

}
