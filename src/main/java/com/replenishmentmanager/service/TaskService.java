package com.replenishmentmanager.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
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
	
}
