package com.services;

public class SubscribedServiceResource {

	private String name;

	private double price;

	private int remaincontent;

	private String startDate;
	
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


}
