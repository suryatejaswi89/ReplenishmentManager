package com.replenishmentmanager.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.replenishmentmanager.model.Task;
import com.replenishmentmanager.model.TaskRepository;
import com.replenishmentmanager.model.User;
import com.replenishmentmanager.model.UserRepository;
import com.replenishmentmanager.service.TaskService;


@Controller
public class TaskController {
	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserRepository userRepo;
	
	
	@RequestMapping(value = "/task", method = RequestMethod.POST)
	@ResponseBody
	public Task createTask(@RequestBody Task task) {
		String description = task.getDescription();
		String taskOwner = task.getTaskOwnerID();
		String assignee = task.getAssigneeID();
		Date dateCreated = task.getDateCreated();
		String status = task.getStatus();
		int estimate = task.getEstimate();
		return taskService.createTask(description, taskOwner, assignee,dateCreated,status,estimate);
	}
	
	@RequestMapping(value="/{status}/", method = RequestMethod.GET)
	@ResponseBody
	public List<Task> getTasksbystatus(@PathVariable(value = "status") String status){
		List<Task> tasks = taskRepo.findAllBystatus(status);
		return tasks;
	}
	
	@RequestMapping(value="/tasks/{_id}/", method = RequestMethod.GET)
	@ResponseBody
	public Optional<Task> getTasksbyID(@PathVariable(value = "_id") String id){
		Optional<Task> task = taskRepo.findById(id);
		return task;
	}
	
	
	@RequestMapping(value="/tasks/", method=RequestMethod.GET)
	@ResponseBody
	public List<Task> getAllTasks(){
		List<Task> tasks = taskRepo.findAll();
		return tasks;
		
	}
	
	@RequestMapping(value =  "/createdByUser/{name}/", method = RequestMethod.GET)
	@ResponseBody
	public List<Task> getAllTasksbyUser(@PathVariable(value = "name") String name){
		 
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		List<Task> tasks = taskRepo.findBytaskOwnerID(id);
		return tasks;
	}
	
	@RequestMapping(value =  "/assignedToUser/{name}/", method = RequestMethod.GET)
	@ResponseBody
	public List<Task> getTasksAssigned(@PathVariable(value = "name") String name){
		 
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		List<Task> tasks = taskRepo.findByassigneeID(id);
		return tasks;
	}
	
	@RequestMapping(value = "/updateStatus/{id}/{status}/", method = RequestMethod.PUT)
	@ResponseBody
	public Task updateStatus(@PathVariable(value = "id") String id, @PathVariable(value = "status") String status) {
		Task task = null;
		Optional<Task> taskWrapper = taskRepo.findById(id);
		if(taskWrapper.isPresent()){
			task = taskWrapper.get();
			task.setStatus(status);
			taskRepo.save(task);
		}else {
			//log error that task is null
		}
		
		return task;
	}
	
	@RequestMapping(value = "/deletetask/{id}/" , method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteTask(@PathVariable(value="id") String id){
		if(taskRepo.existsById(id)){
		taskRepo.deleteById(id);
		return "successfully deleted the task";	
		}
		else{
			return "task not found";
		}
				
	}
	

	
	
	
	
	

}
