package com.matheusmarqs1.customer_api.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import com.matheusmarqs1.customer_api.entities.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_customer")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, unique = true)
	private String cpf;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	private LocalDate birthDate;
	@Column(nullable = false)
	private String phone;
	@Column(nullable=false)
	private String password;
	@Column(nullable = false)
	private Integer role;
	
	public Customer() {
	}

	public Customer(Long id, String name, String cpf, String email, LocalDate birthDate, String phone, String password, Role role) {
		super();
		this.id = id;
		this.name = name;
		this.cpf = cpf;
		this.email = email;
		this.birthDate = birthDate;
		this.phone = phone;
		this.password = password;
		setRole(role);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Role getRole() {
		return Role.valueOf(role);
	}

	public void setRole(Role role) {
		if(role != null) {
			this.role = role.getCode();
		}
	}

	public int getAge() {
		LocalDate currentDate = LocalDate.now();
		int age = Period.between(birthDate, currentDate).getYears();
		return age;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(id, other.id);
	}
	
}
