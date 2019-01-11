package com.services;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class AvailableService {

	@NotNull
	@Min(1)
	private int availableContent;

	public int getAvailableContent() {
		return availableContent;
	}

	public void setAvailableContent(int availableContent) {
		this.availableContent = availableContent;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	@JsonIgnore
	private long id;
	
	@NotNull
	@Size(min = 2, message = "Name should have atleast 2 characters")
	private String name;

	@NotNull
	@Min(1)
	private double price;

	public long getId() {
		return id;
	}
	@JsonIgnore
	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public boolean equals(Object obj) {
		AvailableService service = (AvailableService)obj;
		return service.getName().equalsIgnoreCase(this.getName()) ;
		
	}
	
}
