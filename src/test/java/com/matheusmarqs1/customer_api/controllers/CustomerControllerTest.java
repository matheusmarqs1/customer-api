package com.matheusmarqs1.customer_api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerCreateRequest;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerResponse;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerUpdateRequest;
import com.matheusmarqs1.customer_api.services.CustomerService;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

	@MockitoBean
	private CustomerService customerService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	private CustomerResponse customer1Response;
	private CustomerResponse customer2Response;
	private static final Long EXISTING_CUSTOMER1_ID = 1L;
	private static final Long EXISTING_CUSTOMER2_ID = 2L;
	private static final Long NEW_CUSTOMER_ID = 3L;
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 10;

	@BeforeEach
	void setup() {
		customer1Response = new CustomerResponse(EXISTING_CUSTOMER1_ID, "Alice", "01234567890",
				"alice@example.com", LocalDate.of(1991, 10, 12), "11987654320",
				Period.between(LocalDate.of(1991, 10, 12), LocalDate.now()).getYears());
		
		customer2Response = new CustomerResponse(EXISTING_CUSTOMER2_ID, "Bob", "12345678900",
				"bob@example.com", LocalDate.of(1997, 8, 9), "11987654323",
				Period.between(LocalDate.of(1997, 8, 9), LocalDate.now()).getYears());
	}

	@Test
	@DisplayName("Should return all customers paged")
	void shouldReturnAllCustomersInPage() throws Exception {
		
		Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
		Page<CustomerResponse> page = new PageImpl<>(List.of(customer1Response, customer2Response), pageable, 2);
		
		when(customerService.findCustomers(any(), any(), any(), any(), any(), eq(pageable))).thenReturn(page);
		
		mockMvc.perform(get("/customers"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()").value(2))
		.andExpect(jsonPath("$.content[0].id").value(customer1Response.id()));
		
		verify(customerService).findCustomers(any(), any(), any(), any(), any(), eq(pageable));
	}
	
	@Test
	@DisplayName("Should return customers filtered by name")
	void shouldReturnCustomersFilteredByName() throws Exception {
		Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);
		Page<CustomerResponse> page = new PageImpl<>(List.of(customer1Response), pageable, 1);
		
		when(customerService.findCustomers(eq("Alice"), any(), any(), any(), any(), eq(pageable))).thenReturn(page);
		
		mockMvc.perform(get("/customers").param("name", "Alice"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()").value(1))
		.andExpect(jsonPath("$.content[0].name").value("Alice"));
		
		verify(customerService).findCustomers(eq("Alice"), any(), any(), any(), any(), eq(pageable));
	}

	@Test
	@DisplayName("Should return a customer by valid id")
	void shouldReturnCustomerById() throws Exception {

		when(customerService.findCustomerById(EXISTING_CUSTOMER1_ID)).thenReturn(customer1Response);

		mockMvc.perform(get("/customers/{id}", EXISTING_CUSTOMER1_ID))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(customer1Response.id().toString()));
		
		verify(customerService).findCustomerById(EXISTING_CUSTOMER1_ID);
	}

	@Test
	@DisplayName("Should create a customer when request is valid")
	void shouldCreateCustomerWithValidRequest() throws JsonProcessingException, Exception {
		CustomerCreateRequest createRequest = new CustomerCreateRequest("João Silva", "12345678909", "joao@example.com",
				LocalDate.of(1990, 5, 15), "11987654321", "NewPass#123");

		CustomerResponse createdResponse = new CustomerResponse(NEW_CUSTOMER_ID, createRequest.name(), createRequest.cpf(),
				createRequest.email(), createRequest.birthDate(), createRequest.phone(),
				Period.between(createRequest.birthDate(), LocalDate.now()).getYears());

		when(customerService.createCustomer(any(CustomerCreateRequest.class))).thenReturn(createdResponse);

		mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("João Silva"))
				.andExpect(jsonPath("$.email").value("joao@example.com"));

		verify(customerService).createCustomer(any(CustomerCreateRequest.class));

	}

	@Test
	@DisplayName("Should return 400 when creating customer with blank name")
	void shouldReturnBadRequestWhenNameIsBlank() throws JsonProcessingException, Exception {
	
		mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		verify(customerService, never()).createCustomer(any(CustomerCreateRequest.class));

	}

	@Test
	@DisplayName("Should return 400 when creating customer with invalid email")
	void shouldReturnBadRequestWhenEmailIsInvalid() throws JsonProcessingException, Exception {
		
		mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		verify(customerService, never()).createCustomer(any(CustomerCreateRequest.class));
	}

	@Test
	@DisplayName("Should update a customer when request is valid")
	void shouldUpdateCustomerWithValidRequest() throws JsonProcessingException, Exception {

		CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("João Silva Oliveira", "joaool@example.com",
				LocalDate.of(1990, 5, 15), "11987654321", "NewPass#123"

		);

		CustomerResponse updatedResponse = new CustomerResponse(EXISTING_CUSTOMER1_ID, updateRequest.name(), "12345678909",
				updateRequest.email(), updateRequest.birthDate(), updateRequest.phone(),
				Period.between(updateRequest.birthDate(), LocalDate.now()).getYears()

		);

		when(customerService.updateCustomer(EXISTING_CUSTOMER1_ID, updateRequest)).thenReturn(updatedResponse);

		mockMvc.perform(put("/customers/{id}", EXISTING_CUSTOMER1_ID).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("João Silva Oliveira"))
				.andExpect(jsonPath("$.email").value("joaool@example.com"));

		verify(customerService).updateCustomer(eq(EXISTING_CUSTOMER1_ID), any(CustomerUpdateRequest.class));
	}

	@Test
	@DisplayName("Should delete a customer by id")
	void shouldDeleteCustomerById() throws Exception {

		doNothing().when(customerService).deleteCustomer(EXISTING_CUSTOMER1_ID);

		mockMvc.perform(delete("/customers/{id}", EXISTING_CUSTOMER1_ID)).andExpect(status().isNoContent());

		verify(customerService).deleteCustomer(EXISTING_CUSTOMER1_ID);
	}

}
