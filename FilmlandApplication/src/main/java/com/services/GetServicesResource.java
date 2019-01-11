package com.services;

import java.util.HashSet;
import java.util.Set;

public class GetServicesResource {

	private Set<AvailableService> availableServices = new HashSet<>();
	
	private Set<SubscribedServiceResource> subscribedServices = new HashSet<>();

	public Set<AvailableService> getAvailableServices() {
		return availableServices;
	}

	public void setAvailableServices(Set<AvailableService> availableServices) {
		this.availableServices = availableServices;
	}

	public Set<SubscribedServiceResource> getSubscribedServices() {
		return subscribedServices;
	}

	public void setSubscribedServices(Set<SubscribedServiceResource> subscribedServices) {
		this.subscribedServices = subscribedServices;
	}

	
}
