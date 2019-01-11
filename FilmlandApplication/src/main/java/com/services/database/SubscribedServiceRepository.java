package com.services.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.services.AvailableService;
import com.services.CustomerResource;
import com.services.SubscribedService;

@Repository
public interface SubscribedServiceRepository extends JpaRepository<SubscribedService, CustomerResource> {
	
	public List<SubscribedService> findAllByCustomerResource(CustomerResource customerResource);
	
	public SubscribedService findByCustomerResourceAndAvailableService(CustomerResource customerResource, AvailableService availableService);

}
