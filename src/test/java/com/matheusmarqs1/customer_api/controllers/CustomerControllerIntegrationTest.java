package com.matheusmarqs1.customer_api.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerCreateRequest;
import com.matheusmarqs1.customer_api.dtos.customer.CustomerUpdateRequest;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Customer API endpoints")
@Tag("Integration")
public class CustomerControllerIntegrationTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	@Test
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomersWhenNoFiltersAreApplied() throws Exception {
		mockMvc.perform(get("/customers"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()").value(3));
		
	}
	
	@Test
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomersWhenNameFilterIsApplied() throws Exception {
		mockMvc.perform(get("/customers").param("name", "teles"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()").value(2));
	}
	
	@Test
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomersWhenMoreThanOneFilterAreApplied() throws Exception {
		mockMvc.perform(get("/customers").param("name", "teles").param("email", "ana@example.com"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()").value(1))
		.andExpect(jsonPath("$.content[0].name").value("Ana Teles"));
	}
	
	@Test
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomersWithPagination() throws Exception {
		mockMvc.perform(get("/customers").param("page", "0").param("size", "2"))
		.andExpect(status().isOk()).andExpect(jsonPath("$.content.length()").value(2));
	}
	
	@Test
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomerByIdSuccess() throws Exception {
		mockMvc.perform(get("/customers/{id}", 2))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(2L))
		.andExpect(jsonPath("$.name").value("Ana Teles"));
	}
	
	@Test
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomerByIdNotFound() throws Exception {
		mockMvc.perform(get("/customers/{id}", 999L))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.error").value("Resource not found"))
		.andExpect(jsonPath("$.message").value("Customer not found with ID: " + 999L));
	}
	
	@Test
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomerWithInvalidIdFormat() throws Exception {
		mockMvc.perform(get("/customers/{id}", "abc"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error").value("Invalid argument type"))
		.andExpect(jsonPath("$.message").value(containsString("Parameter 'id'")));
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "customer@example.com", authorities = {"SCOPE_ROLE_CUSTOMER"})
	void testFindCustomersForbiddenAccess() throws Exception {
		mockMvc.perform(get("/customers"))
		.andExpect(status().isForbidden())
		.andExpect(jsonPath("$.error").value("Access Denied"))
		.andExpect(jsonPath("$.message").value("You do not have permission to access this resource"));
	}
	
	@Test
	@Transactional
	void testCreateCustomerSuccess() throws Exception {
		CustomerCreateRequest createRequest = new CustomerCreateRequest(
				"João Pedro", 
				"00000000191", 
				"joaopedro@example.com", 
				LocalDate.of(2001, 8, 6), 
				"1199999999", 
				"AlkPQ12@");
		
		String json = objectMapper.writeValueAsString(createRequest);
		
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", isA(Number.class)))
		.andExpect(jsonPath("$.name").value("João Pedro"))
		.andExpect(jsonPath("$.email").value("joaopedro@example.com"));
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testFindCustomersAfterCreate() throws Exception {
		CustomerCreateRequest createRequest = new CustomerCreateRequest(
				"João Pedro", 
				"00000000191", 
				"joaopedro@example.com", 
				LocalDate.of(2001, 8, 6), 
				"1199999999", 
				"AlkPQ12@");
		
		String json = objectMapper.writeValueAsString(createRequest);
		
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", isA(Number.class)))
		.andExpect(jsonPath("$.name").value("João Pedro"))
		.andExpect(jsonPath("$.email").value("joaopedro@example.com"));
		
		mockMvc.perform(get("/customers"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()").value(4));
	}
	@Test
	@Transactional
	void testCreateCustomerWithInvalidInput() throws Exception {
		CustomerCreateRequest createRequest = new CustomerCreateRequest(
				"João Pedro", 
				"00000000191", 
				"joaopedroexample.com", // e.g. Invalid email format
				LocalDate.of(2001, 8, 6), 
				"1199999999", 
				"AlkPQ12@");
		
		String json = objectMapper.writeValueAsString(createRequest);
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error").value("Validation failed"))
		.andExpect(jsonPath("$.message").value("email: Invalid email format"));
		
	}
	
	@Test
	@Transactional
	void testCreateCustomerWithMoreThanOneInvalidInput() throws Exception {
		CustomerCreateRequest createRequest = new CustomerCreateRequest(
				" ", // e.g. name is required
				"12345678900", // e.g. Invalid cpf format
				"joaopedroexample.com", // e.g. Invalid email format
				LocalDate.of(2001, 8, 6), 
				"11999999", // e.g. Invalid telephone
				"AlkPQ12@");
		
		String json = objectMapper.writeValueAsString(createRequest);
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error").value("Validation failed"))
		.andExpect(jsonPath("$.message", containsString("name: Name is required")))
		.andExpect(jsonPath("$.message", containsString("cpf: Invalid CPF")))
		.andExpect(jsonPath("$.message", containsString("email: Invalid email format")))
		.andExpect(jsonPath("$.message", containsString("phone: Phone number must be between 10 and 11 digits")));
		
	}
	
	@Test
	@Transactional
	void testCreateCustomerWhenEmailAlreadyExists() throws Exception {
		CustomerCreateRequest createRequest = new CustomerCreateRequest(
				"João Pedro", 
				"00000000191", 
				"joao@example.com",  // Email already exists
				LocalDate.of(2001, 8, 6), 
				"1199999999", 
				"AlkPQ12@");
		
		String json = objectMapper.writeValueAsString(createRequest);
		
		mockMvc.perform(post("/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error").value("Business rule violation"))
		.andExpect(jsonPath("$.message", containsString("Email already exists")));
		
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testUpdateCustomerSuccess() throws Exception {
		CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
				"Matheus", 
				"matheust@example.com", 
				LocalDate.of(2005, 8, 28), 
				"99999999910", 
				"AlkMR12@");
		
		String json = objectMapper.writeValueAsString(updateRequest);
		mockMvc.perform(put("/customers/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))
		.andExpect(jsonPath("$.name").value("Matheus"))
		.andExpect(jsonPath("$.phone").value("99999999910"));
		
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testUpdateCustomerWithInvalidInput() throws Exception {
		CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
				"Ana", 
				"anaexample.com", 
				LocalDate.of(2005, 8, 28), 
				"99999999910", 
				"AlkMR12@");
		
		String json = objectMapper.writeValueAsString(updateRequest);
		mockMvc.perform(put("/customers/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error").value("Validation failed"))
		.andExpect(jsonPath("$.message").value("email: Invalid email format"));
		
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testUpdateCustomerWithMoreThanOneInvalidInput() throws Exception {
		CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
				" ", // Name is required
				"ana@example.com",
				LocalDate.of(2005, 8, 28), 
				"999999999", // Invalid phone format
				"AlkMR1"); // Invalid password format
		
		String json = objectMapper.writeValueAsString(updateRequest);
		mockMvc.perform(put("/customers/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error").value("Validation failed"))
		.andExpect(jsonPath("$.message", containsString("name: Name is required")))
		.andExpect(jsonPath("$.message", containsString("phone: Phone number must be between 10 and 11 digits")))
		.andExpect(jsonPath("$.message", containsString("password: Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")));
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testUpdateCustomerWithNonExistingId() throws Exception {
		
		CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
				"Matheus", 
				"matheus@example.com", 
				LocalDate.of(2005, 8, 28), 
				"99999999910", 
				"AlkMR12@");
		
		String json = objectMapper.writeValueAsString(updateRequest);
		
		mockMvc.perform(put("/customers/{id}", 999L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.error").value("Resource not found"))
		.andExpect(jsonPath("$.message").value("Customer not found with ID: " + 999L));
		
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testUpdateCustomerWhenEmailIsAlreadyInUseBySomeoneElse() throws Exception {
		CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
				"Ana Teles", 
				"matheus@example.com", 
				LocalDate.of(1995, 3, 15), 
				"99999999910", 
				"AlkMR12@");
		
		String json = objectMapper.writeValueAsString(updateRequest);
			
		mockMvc.perform(put("/customers/{id}", 2L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.error").value("Business rule violation"))
		.andExpect(jsonPath("$.message").value("Email already exists"));
		
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testDeleteCustomerSuccess() throws Exception {
		mockMvc.perform(delete("/customers/{id}", 1L))
		.andExpect(status().isNoContent());
	}
	
	@Test
	@Transactional
	@WithMockUser(username = "admin@example.com", authorities = {"SCOPE_ROLE_ADMIN"})
	void testDeleteCustomerNotFound() throws Exception {
		mockMvc.perform(delete("/customers/{id}", 999L))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.error").value("Resource not found"))
		.andExpect(jsonPath("$.message").value("Customer not found with ID: " + 999L));
	
	}
	

	
	
}
