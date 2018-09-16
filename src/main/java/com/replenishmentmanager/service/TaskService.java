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
import com.replenishmentmanager.model.User;
import com.replenishmentmanager.model.UserRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public Task saveTask(Task task){
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
	
	public List<Task> getTasksbyCreator(String name){
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		List<Task> tasks = taskRepo.findBytaskOwnerID(id);
		return tasks;
	}
	
	public List<Task> getTasksAssigned(String name){
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		List<Task> tasks = taskRepo.findByassigneeID(id);
		return tasks;
	}
	
	public Task updateStatus(String id, String status){
		Task task = null;
		Optional<Task> taskwrapper = taskRepo.findById(id);
		task = taskwrapper.get();
		task.setStatus(status);
		taskRepo.save(task);
		return task;
	}
	
	
	
	
}
