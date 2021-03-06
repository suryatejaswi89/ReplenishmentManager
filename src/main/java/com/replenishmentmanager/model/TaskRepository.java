package com.replenishmentmanager.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {

	public Optional<Task> findById(String id);
	public List<Task> findBytaskOwnerID(String id);
	public List<Task> findByassigneeID(String id);
	public List<Task> findAllBystatus(String status);
	public void deleteById(String id);
}
