package com.replenishmentmanager.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.replenishmentmanager.model.Task;
import com.replenishmentmanager.model.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepo;
	
	public Task createTask(String description,String taskOwnerID,String assigneeID,Date dateCreated,String status,int estimate){
		Task task = new Task();
		task.setDescription(description);
		task.setTaskOwnerID(taskOwnerID);
		task.setAssigneeID(assigneeID);
		task.setDateCreated(dateCreated);
		task.setStatus("created");
		task.setEstimate(estimate);
		taskRepo.save(task);
		return task;
	}
	
	public List<Task> getTaskbystatus(String status){
		 List<Task> tasks = taskRepo.findAllBystatus(status);
		return tasks;
	}

	public Optional<Task> getTaskbyID(String id){
		Optional<Task> task = taskRepo.findById(id);
		return task;
	}
	
	public List<Task> getAllTasks(){
		List<Task> tasks = taskRepo.findAll();
		return tasks;
	}
	
	
}
