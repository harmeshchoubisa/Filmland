package com.services;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CustomerInputResource {

	@NotNull
	@Size(min=7, max=18, message="Password should have atleast 7 and max 18 characters")
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@NotNull
	@Size(min=2, max=50, message="Email should have atleast 2 and max 50 characters")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
