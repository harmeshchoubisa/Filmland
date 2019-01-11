package com.services;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class SubscribedService {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	public CustomerResource getCustomerResource() {
		return customerResource;
	}

	public void setCustomerResource(CustomerResource customerResource) {
		this.customerResource = customerResource;
	}

	public AvailableService getAvailableService() {
		return availableService;
	}

	public void setAvailableService(AvailableService availableService) {
		this.availableService = availableService;
	}

	@ManyToOne
	@JoinColumn
	private CustomerResource customerResource;
	
	@ManyToOne
	@JoinColumn
	private AvailableService availableService;

	@NotNull
	@Min(1)
	private int remaincontent;
	
	public int getRemaincontent() {
		return remaincontent;
	}

	public void setRemaincontent(int remaincontent) {
		this.remaincontent = remaincontent;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@NotNull
	@Size(min = 2, message = "Date should have valid format")
	private String startDate;

}
