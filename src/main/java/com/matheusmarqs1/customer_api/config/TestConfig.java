package com.matheusmarqs1.customer_api.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.matheusmarqs1.customer_api.entities.Customer;
import com.matheusmarqs1.customer_api.entities.enums.Role;
import com.matheusmarqs1.customer_api.repositories.CustomerRepository;

@Configuration()
@Profile("test")
public class TestConfig implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
	
	private final CustomerRepository customerRepository;
	
	public TestConfig(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		Customer c1 = new Customer(null, "Matheus Teles", "999999999", "matheus@example.com",
				LocalDate.of(2005, 8, 28), "99999999999", passwordEncoder.encode("1234"), Role.ROLE_CUSTOMER);
        Customer c2 = new Customer(null, "Ana Teles", "999999998", "ana@example.com",
                LocalDate.of(1995, 3, 15), "99999999998", passwordEncoder.encode("1234"), Role.ROLE_CUSTOMER);
        Customer c3 = new Customer(null, "João Ferreira", "999999997", "joao@example.com",
                LocalDate.of(1980, 12, 5), "99999999997", passwordEncoder.encode("1234"), Role.ROLE_ADMIN);
        
        customerRepository.saveAll(Arrays.asList(c1, c2, c3));
		
	}
}
