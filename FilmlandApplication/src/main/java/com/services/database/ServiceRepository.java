package com.services.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.services.AvailableService;

@Repository
public interface ServiceRepository extends JpaRepository<AvailableService, Long> {
	
	public AvailableService findByName(String name);

}
