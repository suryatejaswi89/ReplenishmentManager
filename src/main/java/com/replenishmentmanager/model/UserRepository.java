package com.replenishmentmanager.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
	
	public User findByuserId(String id);
	public User findByfName(String fname);
	

}
