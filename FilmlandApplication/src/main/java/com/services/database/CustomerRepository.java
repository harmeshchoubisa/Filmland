package com.services.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.services.CustomerResource;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerResource, Long> {
	
 public CustomerResource findByEmail(String email);
 
 public CustomerResource findByPassword(String password);
}
