package com.matheusmarqs1.customer_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.matheusmarqs1.customer_api.dtos.customer.CustomerCreateRequest;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerResponse;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerUpdateRequest;
import com.matheusmarqs1.customer_api.entities.Customer;
import com.matheusmarqs1.customer_api.entities.enums.Role;
import com.matheusmarqs1.customer_api.repositories.CustomerRepository;
import com.matheusmarqs1.customer_api.services.exceptions.BusinessException;
import com.matheusmarqs1.customer_api.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for Customer API Service")
@Tag("Unit")
public class CustomerServiceTest {
	
	@Mock
	private CustomerRepository customerRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private CustomerService customerService;
	
	private CustomerCreateRequest createRequest;
	private Customer existingCustomer;
	private CustomerUpdateRequest updateRequest;
	private Customer otherExistingCustomer;
	
	private static final String ENCODED_PASSWORD = "encrypted_password";
	private static final Long EXISTING_CUSTOMER_ID = 1L;
	private static final Long OTHER_EXISTING_CUSTOMER_ID = 2L;
	private static final Long NEW_CUSTOMER_ID = 3L;
	private static final Long NON_EXISTING_ID = 999L;
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 10;

	
	@BeforeEach
	void setup() {
		
		createRequest = new CustomerCreateRequest(
				"João Silva",  
		        "12345678901",
		        "joao@example.com",
		        LocalDate.of(1990, 5, 15), 
		        "11987654321", 
		        "NewPass#123"
		);
		
		updateRequest = new CustomerUpdateRequest(
				"Ronaldo Rosa Oliveira",
	 			"ronaldo@example.com",
	 			LocalDate.of(1984, 10, 10),
	 			"11987654322",
	 			"1Ab#3291"
		);
		
		existingCustomer = new Customer(
				EXISTING_CUSTOMER_ID,
				"Ronaldo Rosa", 
				"12345678900", 
				"ronaldorosa@example.com", 
				LocalDate.of(1984, 10, 10), 
				"11987654320", 
				ENCODED_PASSWORD,
				Role.ROLE_CUSTOMER
		);
		
		otherExistingCustomer = new Customer(
				OTHER_EXISTING_CUSTOMER_ID,
				"Ronaldo Fenomeno",
				"12345678903",
				"ronaldo@example.com",
				LocalDate.of(1994, 12, 13),
				"11987654327",
				ENCODED_PASSWORD,
				Role.ROLE_CUSTOMER
		);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Should return all customers when no filters are provided")
	void shouldReturnAllCustomersInPage() {
		
		Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
		
		Page<Customer> page = new PageImpl<>(List.of(existingCustomer, otherExistingCustomer), pageable, 2);
		
		when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
		
		Page<CustomerResponse> response = customerService.findCustomers(null, null, null, null, null, pageable);
		
		assertNotNull(response);
		assertEquals(2, response.getTotalElements());
		assertEquals(existingCustomer.getId(), response.getContent().get(0).id());
		assertEquals(existingCustomer.getName(), response.getContent().get(0).name());
		assertEquals(otherExistingCustomer.getId(), response.getContent().get(1).id());
		assertEquals(otherExistingCustomer.getName(), response.getContent().get(1).name());
		
		verify(customerRepository).findAll(any(Specification.class), eq(pageable));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("Should return customers when a filter is provided")
	void shouldReturnCustomersWhenFilteredByCpf() {
		Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
		
		Page<Customer> page = new PageImpl<>(List.of(existingCustomer), pageable, 1);
		
		when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
		
		Page<CustomerResponse> response = customerService.findCustomers(null, "12345678900", null, null, null, pageable);
		
		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		assertEquals(existingCustomer.getId(), response.getContent().get(0).id());
		assertEquals(existingCustomer.getName(), response.getContent().get(0).name());
		
		verify(customerRepository).findAll(any(Specification.class), eq(pageable));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void shouldReturnCustomersWhenMoreFiltersAreApplied() {
		Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
		
		Page<Customer> page = new PageImpl<>(List.of(existingCustomer), pageable, 1);
		
		when(customerRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
		
		Page<CustomerResponse> response = customerService.findCustomers("Ronaldo Rosa",null,"ronaldorosa@example.com", null, null, pageable);
		
		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		assertEquals(existingCustomer.getId(), response.getContent().get(0).id());
		assertEquals(existingCustomer.getName(), response.getContent().get(0).name());
		assertEquals(existingCustomer.getEmail(), response.getContent().get(0).email());
		
		verify(customerRepository).findAll(any(Specification.class), eq(pageable));
	}
	
	@Test
	@DisplayName("Should return a customer when a valid ID is provided")
	void shouldReturnCustomerWhenIdExists() {
		when(customerRepository.findById(EXISTING_CUSTOMER_ID)).thenReturn(Optional.of(existingCustomer));
		
		CustomerResponse response = customerService.findCustomerById(EXISTING_CUSTOMER_ID);
		
		assertNotNull(response);
		assertEquals(EXISTING_CUSTOMER_ID, response.id());
	    assertEquals(existingCustomer.getName(), response.name());
	    assertEquals(existingCustomer.getEmail(), response.email());
	    assertEquals(existingCustomer.getBirthDate(), response.birthDate());
	    assertEquals(existingCustomer.getPhone(), response.phone());
	    
	    verify(customerRepository).findById(EXISTING_CUSTOMER_ID);
	}
	
	@Test
	@DisplayName("Should throw ResourceNotFoundException when the ID does not exist")
	void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		when(customerRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
		
		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class,
				() -> customerService.findCustomerById(NON_EXISTING_ID)
		);
		
		assertEquals("Customer not found with ID: " + NON_EXISTING_ID, exception.getMessage());
		
		verify(customerRepository).findById(NON_EXISTING_ID);
	}
	
	@Test
	@DisplayName("Should create a customer successfully when CPF and email are unique")
	void shouldCreateCustomerWhenCpfAndEmailAreUnique() {
		
		when(customerRepository.findByCpf(createRequest.cpf())).thenReturn(Optional.empty());
		when(customerRepository.findByEmail(createRequest.email())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(createRequest.password())).thenReturn(ENCODED_PASSWORD);
		
		when(customerRepository.save(any(Customer.class)))
	    .thenAnswer(invocation -> {
	        Customer customer = invocation.getArgument(0);
	        customer.setId(NEW_CUSTOMER_ID);
	        return customer;
	    });
		
		CustomerResponse response = customerService.createCustomer(createRequest);
		
		assertNotNull(response);
	    assertEquals(NEW_CUSTOMER_ID, response.id());
	    assertEquals(createRequest.name(), response.name());
	    assertEquals(createRequest.email(), response.email());
		
	    verify(customerRepository).findByCpf(createRequest.cpf());
	    verify(customerRepository).findByEmail(createRequest.email());
	    verify(passwordEncoder).encode(createRequest.password());
	    verify(customerRepository).save(any(Customer.class));
	}
	@Test
	@DisplayName("Should throw BusinessException when creating a customer with an existing CPF")
	void shouldThrowBusinessExceptionWhenCreatingCustomerWithExistingCpf() {
		when(customerRepository.findByCpf(createRequest.cpf())).thenReturn(Optional.of(existingCustomer));
		
		BusinessException exception = assertThrows(
				BusinessException.class, 
				() -> customerService.createCustomer(createRequest)
		);
		
		assertEquals("CPF already exists", exception.getMessage());
		verify(customerRepository).findByCpf(createRequest.cpf());
		verify(customerRepository, never()).findByEmail(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(customerRepository, never()).save(any(Customer.class));
		
	}
	
	@Test
	@DisplayName("Should throw BusinessException when creating a customer with an existing email")
	void shouldThrowBusinessExceptionWhenCreatingCustomerWithExistingEmail() {
		when(customerRepository.findByCpf(createRequest.cpf())).thenReturn(Optional.empty());
		when(customerRepository.findByEmail(createRequest.email())).thenReturn(Optional.of(existingCustomer));
		
		BusinessException exception = assertThrows(
				BusinessException.class,
				() -> customerService.createCustomer(createRequest)
		);
		
		assertEquals("Email already exists", exception.getMessage());
		verify(customerRepository).findByCpf(createRequest.cpf());
		verify(customerRepository).findByEmail(createRequest.email());
		verify(passwordEncoder, never()).encode(anyString());
		verify(customerRepository, never()).save(any(Customer.class));
	}
	
	@Test
	@DisplayName("Should update a customer when the ID exists and the email is unique")
	void shouldUpdateCustomerWhenIdExistsAndEmailIsUnique() {
		when(customerRepository.findById(EXISTING_CUSTOMER_ID)).thenReturn(Optional.of(existingCustomer));
		when(customerRepository.findByEmail(updateRequest.email())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(updateRequest.password())).thenReturn(ENCODED_PASSWORD);
		
		when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
		
		CustomerResponse response = customerService.updateCustomer(EXISTING_CUSTOMER_ID, updateRequest);
	
		
		assertNotNull(response);
		assertEquals(EXISTING_CUSTOMER_ID, response.id());
		assertEquals(updateRequest.name(), response.name());
		assertEquals(updateRequest.email(), response.email());
		assertEquals(updateRequest.birthDate(), response.birthDate());
		assertEquals(updateRequest.phone(), response.phone());
		
		verify(customerRepository).findById(EXISTING_CUSTOMER_ID);
	    verify(customerRepository).findByEmail(updateRequest.email());
	    verify(passwordEncoder).encode(updateRequest.password());
	    verify(customerRepository).save(any(Customer.class));
	}
	
	@Test
	@DisplayName("Should throw ResourceNotFoundException when updating a customer with a non-existing ID")
	void shouldThrowResourceNotFoundExceptionWhenUpdatingCustomerWithNonExistingId() {
		when(customerRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());
		
		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class,
				() -> customerService.updateCustomer(NON_EXISTING_ID, updateRequest)
		);
		
		assertEquals("Customer not found with ID: " + NON_EXISTING_ID, exception.getMessage());
		verify(customerRepository).findById(NON_EXISTING_ID);
		verify(customerRepository, never()).findByEmail(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(customerRepository, never()).save(any(Customer.class));
	}
	
	@Test
	@DisplayName("Should throw BusinessException when the email already exists for another customer")
	void shouldThrowBusinessExceptionWhenEmailAlreadyExistsForAnotherCustomer() {
		when(customerRepository.findById(EXISTING_CUSTOMER_ID)).thenReturn(Optional.of(existingCustomer));
		when(customerRepository.findByEmail(updateRequest.email())).thenReturn(Optional.of(otherExistingCustomer));
		
		BusinessException exception = assertThrows(
				BusinessException.class,
				() -> customerService.updateCustomer(EXISTING_CUSTOMER_ID, updateRequest)
		);
		assertEquals("Email already exists", exception.getMessage());
		verify(customerRepository).findById(EXISTING_CUSTOMER_ID);
		verify(customerRepository).findByEmail(updateRequest.email());
		verify(passwordEncoder, never()).encode(anyString());
		verify(customerRepository, never()).save(any(Customer.class));
	}
	
	@Test
	@DisplayName("Should delete a customer when the ID exists")
	void shouldDeleteCustomerWhenIdExists() {
		when(customerRepository.existsById(EXISTING_CUSTOMER_ID)).thenReturn(true);
		
		customerService.deleteCustomer(EXISTING_CUSTOMER_ID);
		
		verify(customerRepository).existsById(EXISTING_CUSTOMER_ID);
		verify(customerRepository).deleteById(EXISTING_CUSTOMER_ID);
	}
	
	@Test
	@DisplayName("Should throw ResourceNotFoundException when deleting a customer with a non-existing ID")
	void shouldThrowResourceNotFoundExceptionWhenDeletingCustomerWithNonExistingId() {
		when(customerRepository.existsById(NON_EXISTING_ID)).thenReturn(false);
		
		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class, 
				() -> customerService.deleteCustomer(NON_EXISTING_ID)
		);
		
		assertEquals("Customer not found with ID: " + NON_EXISTING_ID, exception.getMessage());
		verify(customerRepository).existsById(NON_EXISTING_ID);
		verify(customerRepository, never()).deleteById(anyLong());
		
	}
	
}
