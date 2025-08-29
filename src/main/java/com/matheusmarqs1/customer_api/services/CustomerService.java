package com.matheusmarqs1.customer_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matheusmarqs1.customer_api.dtos.customer.CustomerCreateRequest;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerResponse;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerUpdateRequest;
import com.matheusmarqs1.customer_api.entities.Customer;
import com.matheusmarqs1.customer_api.exceptions.BusinessException;
import com.matheusmarqs1.customer_api.exceptions.ResourceNotFoundException;
import com.matheusmarqs1.customer_api.repositories.CustomerRepository;

@Service
public class CustomerService {
	
	private final CustomerRepository customerRepository;
	
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	public Page<CustomerResponse> findAllCustomers(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<Customer> customersPage = customerRepository.findAll(pageable);
		return customersPage.map(CustomerResponse::fromEntity);
				
	}
	
	public CustomerResponse findCustomerById(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
		
		return CustomerResponse.fromEntity(customer);
	}
	
	public CustomerResponse createCustomer(CustomerCreateRequest createRequest) {
		if(customerRepository.findByCpf(createRequest.cpf()).isPresent()) {
			throw new BusinessException("CPF already exists");
		}
		
		if(customerRepository.findByEmail(createRequest.email()).isPresent()) {
			throw new BusinessException("Email already exists");
		}
		
		Customer customer = new Customer(null, 
				createRequest.name(),
				createRequest.cpf(),
				createRequest.email(),
				createRequest.birthDate(),
				createRequest.phone());
		
		Customer savedCustomer = customerRepository.save(customer);
		return CustomerResponse.fromEntity(savedCustomer);
		
	}
	
	public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest updateRequest) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
		
		customerRepository.findByEmail(updateRequest.email()).ifPresent(existingCustomer -> {
			if(!existingCustomer.getId().equals(id)) {
				throw new BusinessException("Email already exists");
			}
		});
		
		customer.setName(updateRequest.name());
		customer.setEmail(updateRequest.email());
		customer.setBirthDate(updateRequest.birthDate());
		customer.setPhone(updateRequest.phone());
		
		Customer savedCustomer = customerRepository.save(customer);
		return CustomerResponse.fromEntity(savedCustomer);
		
	}
	
	public void deleteCustomer(Long id) {
		if(!customerRepository.existsById(id)) {
			throw new ResourceNotFoundException("Customer not found with ID: " + id);
		}
		
		customerRepository.deleteById(id);
	}
}
