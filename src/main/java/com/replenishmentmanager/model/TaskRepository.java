package com.replenishmentmanager.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
	
	
	public Task findBytaskID(String id);
	public List<Task> findBytaskOwnerID(String id);
	public List<Task> findByassigneeID(String id);
	
	

}
