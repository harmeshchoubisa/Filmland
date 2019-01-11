package com.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.services.database.CustomerRepository;
import com.services.database.ServiceRepository;
import com.services.database.SubscribedServiceRepository;
import com.services.exception.FilmlandAuthorizeException;

/**
 * 
 * @author HCHOUBIS Class FilmlandApplicationController is responsible for
 *         customers login and payment subscriptions
 */
@RestController
@RequestMapping(value = "/filmland")
public class FilmlandApplicationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilmlandApplicationController.class);

	private CustomerRepository customerRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private SubscribedServiceRepository subscribedServiceRepository;

	@Autowired
	public FilmlandApplicationController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;

	}

	private SubscribedServiceResource populateSubscribedServiceResource(SubscribedService subscribedService) {
		SubscribedServiceResource subscribedServiceResource = new SubscribedServiceResource();
		subscribedServiceResource.setName(subscribedService.getAvailableService().getName());
		subscribedServiceResource.setRemaincontent(subscribedService.getAvailableService().getAvailableContent());
		subscribedServiceResource.setPrice(subscribedService.getAvailableService().getPrice());
		subscribedServiceResource.setStartDate(subscribedService.getStartDate());
		return subscribedServiceResource;
	}

	private void filterAvailableServices(List<AvailableService> availableServices,
			List<SubscribedService> subscribedServices) {
		for (SubscribedService subscribedService : subscribedServices) {
			for (Iterator<AvailableService> iter = availableServices.iterator(); iter.hasNext();) {
				AvailableService allService = iter.next();
				if (allService.getName().equalsIgnoreCase(subscribedService.getAvailableService().getName())) {
					iter.remove();
				}
			}
		}
	}

	@PostMapping(path = "/login")
	public BuyServiceOutputResource customerAuthentication(
			@Valid @RequestBody CustomerInputResource customerInputResource) {
		final String LOG_METHOD = "customerAuthentication";
		LOGGER.info(LOG_METHOD, customerInputResource);
		BuyServiceOutputResource buyServiceOutputResource = new BuyServiceOutputResource();
		CustomerResource customerEmail = customerRepository.findByEmail(customerInputResource.getEmail());
		CustomerResource customerPassword = customerRepository.findByPassword(customerInputResource.getPassword());
		if (customerEmail != null && customerPassword != null) {
			buyServiceOutputResource.setStatus(Constants.SUCCESS);
			buyServiceOutputResource.setMessage(Constants.WELCOME_TO_FILMLAND);
			return buyServiceOutputResource;
		} else {
			LOGGER.error(LOG_METHOD, "Username or Password is incorrect");
			throw new FilmlandAuthorizeException(Constants.USERNAME_OR_PASS_INCORRECT);
		}

	}

	@GetMapping(path = "/all")
	public GetServicesResource getAllServices(@RequestParam(value = "email", required = true) String email) {
		final String LOG_METHOD = "getAllServices";
		LOGGER.info(LOG_METHOD, "getAllServices start function");
		GetServicesResource getServicesResource = new GetServicesResource();
		CustomerResource customerResource = customerRepository.findByEmail(email);
		if (customerResource == null) {
			LOGGER.error(LOG_METHOD, "User not authorized");
			throw new FilmlandAuthorizeException(Constants.NOT_AUTHORIZED_TO_VIEW);
		}
		List<AvailableService> availableServices = serviceRepository.findAll();
		List<SubscribedService> subscribedServices = subscribedServiceRepository
				.findAllByCustomerResource(customerResource);
		for (SubscribedService subscribedService : subscribedServices) {
			getServicesResource.getSubscribedServices().add(populateSubscribedServiceResource(subscribedService));
		}
		filterAvailableServices(availableServices, subscribedServices);
		getServicesResource.getAvailableServices().addAll(availableServices);
		LOGGER.info(LOG_METHOD, "getAllServices end function");
		return getServicesResource;

	}

	@PostMapping(path = "/buy")
	public BuyServiceOutputResource buyServices(@Valid @RequestBody BuyServiceInputResource buyServiceInputResource,
			@RequestParam(value = "email", required = true) String email) {
		final String LOG_METHOD = "buyServices";
		LOGGER.info(LOG_METHOD, "buyServices start function");
		BuyServiceOutputResource buyServiceOutputResource = new BuyServiceOutputResource();
		AvailableService service = serviceRepository.findByName(buyServiceInputResource.getName());
		CustomerResource customerResource = customerRepository.findByEmail(email);
		SubscribedService subscribedService = subscribedServiceRepository
				.findByCustomerResourceAndAvailableService(customerResource, service);

		if (subscribedService != null) {
			buyServiceOutputResource.setStatus(Constants.FAILED);
			buyServiceOutputResource.setMessage(Constants.SERVICE_ALREADY_SUBSCRIBED);
			return buyServiceOutputResource;
		}

		SubscribedService newSubscribedService = new SubscribedService();
		newSubscribedService.setCustomerResource(customerResource);
		newSubscribedService.setAvailableService(service);
		newSubscribedService.setRemaincontent(service.getAvailableContent());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		Date paymentDate = cal.getTime();
		newSubscribedService.setStartDate(sdf.format(paymentDate));
		subscribedServiceRepository.save(newSubscribedService);

		buyServiceOutputResource.setStatus(Constants.SUCCESS);
		buyServiceOutputResource.setMessage(Constants.SERVICE_SUBSCRIBED_SUCCESSFUL);

		LOGGER.info(LOG_METHOD, "buyServices end function");

		return buyServiceOutputResource;

	}

	@PostMapping(path = "/share")
	public BuyServiceOutputResource shareServices(
			@Valid @RequestBody ShareServiceInputResource shareServiceInputResource,
			@RequestParam(value = "email", required = true) String email) {
		final String LOG_METHOD = "shareServices";
		LOGGER.info(LOG_METHOD, "shareServices start function");
		BuyServiceOutputResource buyServiceOutputResource = new BuyServiceOutputResource();
		CustomerResource sharedCustomer = customerRepository.findByEmail(shareServiceInputResource.getEmail());
		AvailableService service = serviceRepository.findByName(shareServiceInputResource.getName());
		CustomerResource customerResource = customerRepository.findByEmail(email);
		SubscribedService subscribedService = subscribedServiceRepository
				.findByCustomerResourceAndAvailableService(customerResource, service);

		if (subscribedService == null) {
			buyServiceOutputResource.setStatus(Constants.FAILED);
			buyServiceOutputResource.setMessage(Constants.SERVICE_NOT_SUBSCRIBED);
			return buyServiceOutputResource;
		}

		SubscribedService checksubscribedService = subscribedServiceRepository
				.findByCustomerResourceAndAvailableService(sharedCustomer, service);

		if (checksubscribedService != null) {
			buyServiceOutputResource.setStatus(Constants.FAILED);
			buyServiceOutputResource.setMessage(Constants.SERVICE_ALREADY_SUBSCRIBED);
			return buyServiceOutputResource;
		}

		SubscribedService newSubscribedService = new SubscribedService();
		newSubscribedService.setCustomerResource(sharedCustomer);
		newSubscribedService.setAvailableService(service);
		newSubscribedService.setRemaincontent(service.getAvailableContent());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		Date paymentDate = cal.getTime();
		newSubscribedService.setStartDate(sdf.format(paymentDate));
		subscribedServiceRepository.save(newSubscribedService);

		buyServiceOutputResource.setStatus(Constants.SUCCESS);
		buyServiceOutputResource.setMessage(Constants.SERVICE_SHARED_SUCCESSFUL);

		LOGGER.info(LOG_METHOD, "shareServices end function");

		return buyServiceOutputResource;

	}

}
