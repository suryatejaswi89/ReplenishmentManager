package com.replenishmentmanager.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.replenishmentmanager.model.Task;
import com.replenishmentmanager.model.TaskRepository;
import com.replenishmentmanager.service.TaskService;


@Controller
public class TaskController {
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private TaskService taskService;
	
	
	@RequestMapping(value = "/task", method = RequestMethod.POST)
	@ResponseBody
	public Task createUser(@RequestBody Task task) {
		String description = task.getDescription();
		String taskOwner = task.getTaskOwnerID();
		String assignee = task.getAssigneeID();
		Date dateCreated = task.getDateCreated();
		String status = task.getStatus();
		int estimate = task.getEstimate();
		
		
		

		return taskService.createTask(description, taskOwner, assignee,dateCreated,status,estimate);
	}
	
	

}
