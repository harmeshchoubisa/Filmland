package com.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.services.AvailableService;
import com.services.CustomerResource;
import com.services.SubscribedService;


@Component
public class DatabaseSeeder implements CommandLineRunner {
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ServiceRepository serviceRepository;
	@Autowired
	private SubscribedServiceRepository subscribedServiceRepository;
	
	@Override
	public void run(String... args) throws Exception {
		AvailableService internationalFilms = new AvailableService();
		internationalFilms.setName("Internationale Films");
		internationalFilms.setPrice(8.0);
		internationalFilms.setAvailableContent(5);
		serviceRepository.save(internationalFilms);
		
		AvailableService netherlandsFilms = new AvailableService();
		netherlandsFilms.setName("Nederlandse Films");
		netherlandsFilms.setPrice(8.0);
		netherlandsFilms.setAvailableContent(5);
		
		serviceRepository.save(netherlandsFilms);
		AvailableService nederlandseSeries = new AvailableService();
		nederlandseSeries.setName("Nederlandse Series");
		nederlandseSeries.setPrice(6.0);
		nederlandseSeries.setAvailableContent(20);
		
		serviceRepository.save(nederlandseSeries);
		
		CustomerResource customerResource1 = addCustomer("temp1@gmail.com", "sdsfdfsf");
		addCustomer("temp2@gmail.com", "sdghgfdf");

		SubscribedService subscribedService = new SubscribedService();
		subscribedService.setCustomerResource(customerResource1);
		subscribedService.setAvailableService(nederlandseSeries);
		subscribedService.setRemaincontent(10);
		subscribedService.setStartDate("01-01-2019");
		subscribedServiceRepository.save(subscribedService);
		
		subscribedService = new SubscribedService();
		subscribedService.setCustomerResource(customerResource1);
		subscribedService.setAvailableService(netherlandsFilms);
		subscribedService.setRemaincontent(10);
		subscribedService.setStartDate("02-01-2019");
		subscribedServiceRepository.save(subscribedService);
		
	}
	
	public CustomerResource addCustomer(String email, String password) {
		CustomerResource customer = new CustomerResource();
		customer.setEmail(email);
		customer.setPassword(password);
		customerRepository.save(customer);
		return customer;
	}

}
