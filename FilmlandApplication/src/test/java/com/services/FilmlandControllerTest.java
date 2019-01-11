package com.services;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.services.database.CustomerRepository;
import com.services.database.ServiceRepository;
import com.services.database.SubscribedServiceRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(FilmlandApplicationController.class)
public class FilmlandControllerTest {

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private ServiceRepository serviceRepository;

	@MockBean
	private SubscribedServiceRepository subscribedServiceRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void customerAuthenricationTest_success() throws Exception {
		CustomerInputResource customerInputResource = new CustomerInputResource();
		customerInputResource.setEmail("temp1@gmail.com");
		customerInputResource.setPassword("sdsfdfsf");

		when(customerRepository.findByEmail("temp1@gmail.com"))
				.thenReturn(new CustomerResource("temp1@gmail.com", "sdsfdfsf"));
		when(customerRepository.findByPassword("sdsfdfsf"))
				.thenReturn(new CustomerResource("temp1@gmail.com", "sdsfdfsf"));

		RequestBuilder request = MockMvcRequestBuilders.post("/filmland/login").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(toJson(customerInputResource));

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
				.andExpect(content().json("{\r\n" + "    \"status\": \"success\",\r\n"
						+ "    \"message\": \"Welcome to Filmland App! Login Successful\"\r\n" + "}"))
				.andReturn();
	}

	public String toJson(Object object) throws Exception {

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(object);
		System.out.println(json);
		return json;

	}

	@Test
	public void customerAuthenricationTest_failed() throws Exception {
		CustomerInputResource customerInputResource = new CustomerInputResource();
		customerInputResource.setEmail("temp3@gmail.com");
		customerInputResource.setPassword("sdsfddsfdsffsf");

		when(customerRepository.findByEmail("temp3@gmail.com")).thenReturn(new CustomerResource());
		when(customerRepository.findByPassword("sdsfdfhgfhgfhsf")).thenReturn(new CustomerResource());

		RequestBuilder request = MockMvcRequestBuilders.post("/filmland/login").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(toJson(customerInputResource));

		MvcResult result = mockMvc.perform(request).andExpect(status().isUnauthorized())
				.andExpect(content().json("{\r\n" + "    \"status\": \"Unauthorized\",\r\n"
						+ "    \"message\": \"Either username or password is incorrect\"\r\n" + "}"))
				.andReturn();
	}

	@Test
	public void getAllServices() throws Exception {
		
		CustomerResource customerResource = new CustomerResource();
		customerResource.setEmail("temp1@gmail.com");
		customerResource.setPassword("sdsfdfsf");

		when(customerRepository.findByEmail("temp1@gmail.com"))
				.thenReturn(new CustomerResource("temp1@gmail.com", "sdsfdfsf"));

		List<AvailableService> availableServices = new ArrayList<>();
		AvailableService internationalFilms = new AvailableService();
		internationalFilms.setName("Internationale Films");
		internationalFilms.setPrice(8.0);
		internationalFilms.setAvailableContent(5);
		availableServices.add(internationalFilms);

		AvailableService netherlandsFilms = new AvailableService();
		netherlandsFilms.setName("Nederlandse Films");
		netherlandsFilms.setPrice(8.0);
		netherlandsFilms.setAvailableContent(5);
		availableServices.add(netherlandsFilms);

		AvailableService nederlandseSeries = new AvailableService();
		nederlandseSeries.setName("Nederlandse Series");
		nederlandseSeries.setPrice(6.0);
		nederlandseSeries.setAvailableContent(20);
		availableServices.add(nederlandseSeries);

		when(serviceRepository.findAll()).thenReturn(availableServices);
		
		List<SubscribedService> subscribedServices = new ArrayList<>();
		SubscribedService subscribedService = new SubscribedService();
		subscribedService.setCustomerResource(customerResource);
		subscribedService.setAvailableService(nederlandseSeries);
		subscribedService.setRemaincontent(10);
		subscribedService.setStartDate("01-01-2019");
		subscribedServices.add(subscribedService);
		
		subscribedService = new SubscribedService();
		subscribedService.setCustomerResource(customerResource);
		subscribedService.setAvailableService(netherlandsFilms);
		subscribedService.setRemaincontent(10);
		subscribedService.setStartDate("02-01-2019");
		subscribedServices.add(subscribedService);
		
		when(subscribedServiceRepository.findAllByCustomerResource(customerResource)).thenReturn(subscribedServices);
		

		RequestBuilder request = MockMvcRequestBuilders.get("/filmland/all").contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").param("email", "temp1@gmail.com");
		
		MvcResult result = mockMvc.perform(request).andExpect(status().isOk())
				.andReturn();
	}

}
