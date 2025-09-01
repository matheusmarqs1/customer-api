package com.matheusmarqs1.customer_api.specifications;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import com.matheusmarqs1.customer_api.entities.Customer;

public class CustomerSpecs {
	
	public static Specification<Customer> byName(String name){
		 return (root, query, builder) -> 
			 (ObjectUtils.isEmpty(name)) ? builder.conjunction() : builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
		 
	}
	
	public static Specification<Customer> byCpf(String cpf){
		return (root, query, builder) ->
			(ObjectUtils.isEmpty(cpf)) ? builder.conjunction() : builder.equal(root.get("cpf"), cpf);
	}
	
	public static Specification<Customer> byEmail(String email){
		return (root, query, builder) -> 
			(ObjectUtils.isEmpty(email)) ? builder.conjunction() : builder.equal(root.get("email"), email);
	}
	
	public static Specification<Customer> byBirthDate(LocalDate birthDate){
		return(root, query, builder) -> 
			(birthDate == null) ? builder.conjunction() : builder.equal(root.get("birthDate"), birthDate);
	}
	
	public static Specification<Customer> byPhone(String phone){
		return(root, query, builder) ->
			(ObjectUtils.isEmpty(phone)) ? builder.conjunction() : builder.equal(root.get("phone"), phone);
	}
	
}
