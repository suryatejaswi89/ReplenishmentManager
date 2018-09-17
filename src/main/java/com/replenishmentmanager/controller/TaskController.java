package com.replenishmentmanager.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	final static Logger logger = LogManager.getLogger();
	
	@RequestMapping(value = "/task", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		try{
			Task newTask = taskService.createTask(task);
			logger.info("Task with id {} got created successfully", newTask.getTaskID());
			logger.debug("Persisted task with id {} and with details {}", newTask.getTaskID(), newTask.toString());
			return new ResponseEntity<Task>(newTask, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Task creation failed with the exception: {}", e.getMessage());
			return new ResponseEntity<Task>(task,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/{status}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getTasksbystatus(@PathVariable(value = "status") String status){
		List<Task> tasks = null;
		try{
		tasks = taskService.getTaskbystatus(status);
		logger.info("Tasks with given status have been found succesfully", tasks);
		logger.debug("tasks with given status {}",tasks.toString());
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("fetching the task with given task has failed", e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/pendingtasks/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> pendingTasks(){
		List<Task> tasks = null;
		try{
		tasks = taskService.getPendingTasks();
		logger.info("Pending tasks have been returned",tasks);
		logger.debug("Pending tasks have been returned",tasks.toString());
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK); 
		}
		catch(Exception e){
			logger.error("Failed to get pending tasks");
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.BAD_REQUEST); 
	}
	
	@RequestMapping(value = "/tasks/{id}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Optional<Task>> getTasksbyID(@PathVariable(value = "id") String id) {
		Optional<Task> task = null;
		try {
			task = taskService.getTaskbyID(id);
			logger.info("Task with given id has been found", task);
			logger.debug("Task with given id has been found", task.toString());
			return new ResponseEntity<Optional<Task>>(task, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Task fetching by Id has failed", e.getMessage());
		}
		return new ResponseEntity<Optional<Task>>(task, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	
	@RequestMapping(value="/tasks/", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getAllTasks(){
		List<Task> tasks = null;
		try{
		 tasks = taskService.getAllTasks();
		 logger.info("All tasks have been listed", tasks);
		logger.debug("All tasks have been listed",tasks.toString());
		 return new ResponseEntity<List<Task>>(tasks,HttpStatus.OK);
		 
		}
		catch(Exception e){
			logger.error("Listing tasks has failed due to an error",e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.SERVICE_UNAVAILABLE);
		
	}
	
	@RequestMapping(value =  "/createdByUser/{name}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getAllTaskscreatedByUser(@PathVariable(value = "name") String name){
		List<Task> tasks = null;
		try{
			tasks = taskService.getTasksbyCreator(name);
		logger.info("All tasks created by user with given id have been listed", tasks);
		logger.debug("All tasks have been listed", tasks.toString());
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("Listing tasks by ownerId have failed",e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value =  "/assignedToUser/{name}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getTasksAssigned(@PathVariable(value = "name") String name){
		List<Task> tasks = null;
		try{
		tasks = taskService.getTasksAssigned(name);
		logger.info("All tasks assigned to user with given id have been listed",tasks);
		logger.debug("All tasks assigned to the have been listed", tasks.toString());
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("Listing tasks by AssigneeID have failed",e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/updateStatus/{id}/{status}/", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Task> updateStatus(@PathVariable(value = "id") String id,
			@PathVariable(value = "status") String status) throws CloneNotSupportedException {
		Task task = null;
		try {
			task = taskService.updateStatus(id, status);
			return new ResponseEntity<Task>(task, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Update status failed with error: {}", e.getMessage());
		}
		return new ResponseEntity<Task>(task, HttpStatus.SERVICE_UNAVAILABLE);
	}
	
	@RequestMapping(value = "/deletetask/{id}/", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteTask(@PathVariable(value = "id") String id) {

		try {
			String str = taskService.deleteTask(id);
			logger.info("Task found in the repo", id);
			return new ResponseEntity<String>(str, HttpStatus.OK);
		} catch (Exception e) {	
			logger.error("Task deletion has failed", taskRepo.findById(id));
		}
		return new ResponseEntity<String>("Task deletion has failed", HttpStatus.SERVICE_UNAVAILABLE);
	}
	
}
