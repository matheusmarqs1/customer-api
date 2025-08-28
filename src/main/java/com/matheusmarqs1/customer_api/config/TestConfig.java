package com.matheusmarqs1.customer_api.config;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.matheusmarqs1.customer_api.entities.Customer;
import com.matheusmarqs1.customer_api.repositories.CustomerRepository;

@Configuration()
@Profile("test")
public class TestConfig implements CommandLineRunner {
	
	private final CustomerRepository customerRepository;
	
	public TestConfig(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Customer c1 = new Customer(null, "Matheus", "999999999", "matheus@example.com",
				LocalDate.of(2005, 8, 28), "99999999999");
        Customer c2 = new Customer(null, "Ana", "999999998", "ana@example.com",
                LocalDate.of(1995, 3, 15), "99999999998");
        Customer c3 = new Customer(null, "João", "999999997", "joao@example.com",
                LocalDate.of(1980, 12, 5), "99999999997");
        
        customerRepository.saveAll(Arrays.asList(c1, c2, c3));
		
	}
}
