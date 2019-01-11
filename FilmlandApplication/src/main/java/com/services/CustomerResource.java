package com.services;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CustomerResource {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	private String email;

	private String password;
	
	
	public CustomerResource() {
		super();
	}

	public CustomerResource(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
