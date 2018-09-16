package com.replenishmentmanager.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.replenishmentmanager.commons.StatusEnum;
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
	
	final static Logger logger = LogManager.getLogger();
	
	@RequestMapping(value = "/task", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Task> createTask(@RequestBody Task task) {

		Task persistedTask = null;
		try{
			task.setStatus(StatusEnum.CREATED.toString());
			task.setWeightage(task.getPriority()*1000/task.getEstimate());
			persistedTask = taskService.saveTask(task);
			logger.info("Task with id {} got created successfully", persistedTask.getTaskID());
			logger.debug("Persisted task with id {} and with details {}", persistedTask.getTaskID(), persistedTask.toString());
			return new ResponseEntity<Task>(persistedTask, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Task creation failed with the exception: {}", e.getMessage());
			return new ResponseEntity<Task>(persistedTask,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/{status}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getTasksbystatus(@PathVariable(value = "status") String status){
		List<Task> tasks = null;
		try{
		tasks = taskRepo.findAllBystatus(status);
		logger.info("Tasks with given status have been found succesfully", tasks);
		logger.debug("tasks with given status {}",tasks.toString());
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		//logger.info(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		}
		catch(Exception e){
			
			logger.error("fething the task with given task has failed", e.getMessage());
			
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/pendingtasks/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> pendingTasks(){
		
		List<Task> pendingTasks = new ArrayList<Task>();
		
		try{
		pendingTasks.addAll(taskRepo.findAllBystatus(StatusEnum.CREATED.toString()));
		pendingTasks.addAll(taskRepo.findAllBystatus(StatusEnum.STARTED.toString()));
	
		List<Task> sortedTasks = pendingTasks.stream()
				.sorted(Comparator.comparingDouble(Task::getWeightage).reversed())
				.collect(Collectors.toList());
		logger.info("Pending tasks have been listed successfully",sortedTasks);
		logger.debug("Pending tasks have been listed successfully",sortedTasks.toString());
		return new ResponseEntity<List<Task>>(sortedTasks, HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("feching the pending tasks has failed", e.getMessage());	
		}
		
		return new ResponseEntity<List<Task>>(pendingTasks, HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(value="/tasks/{id}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Optional<Task>> getTasksbyID(@PathVariable(value = "id") String id){
		Optional<Task> task = null;
		try{
		task = taskRepo.findById(id);
		logger.info("Task with given id has been found", task);
		logger.debug("Task with given id has been found",task.toString());
		return new ResponseEntity<Optional<Task>>(task,HttpStatus.OK);
	}
	catch(Exception e){
		logger.error("Task fetching by Id has failed", e.getMessage());
	}
		return new ResponseEntity<Optional<Task>>(task, HttpStatus.BAD_REQUEST);
	}
	
	
	@RequestMapping(value="/tasks/", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getAllTasks(){
		List<Task> tasks = null;
		try{
		 tasks = taskRepo.findAll();
		 logger.info("All tasks have been listed", tasks);
		logger.debug("All tasks have been listed",tasks.toString());
		 return new ResponseEntity<List<Task>>(tasks,HttpStatus.OK);
		 
		}
		catch(Exception e){
			logger.error("Listing tasks has failed due to an error",e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.BAD_REQUEST);
		
	}
	
	@RequestMapping(value =  "/createdByUser/{name}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getAllTaskscreatedByUser(@PathVariable(value = "name") String name){
		List<Task> tasks = null;
		try{
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		tasks = taskRepo.findBytaskOwnerID(id);
		logger.info("All tasks created by user with given id have been listed", id,tasks);
		logger.debug("All tasks have been listed",id, tasks.toString());
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
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		tasks = taskRepo.findByassigneeID(id);
		logger.info("All tasks assigned to user with given id have been listed", id,tasks);
		logger.debug("All tasks assigned to the have been listed",id, tasks.toString());
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.OK);
		}
		catch(Exception e){
			logger.error("Listing tasks by AssigneeID have failed",e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks,HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/updateStatus/{id}/{status}/", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Task> updateStatus(@PathVariable(value = "id") String id, @PathVariable(value = "status") String status) throws CloneNotSupportedException {
		Task task = null;
		try{
		Optional<Task> taskWrapper = taskRepo.findById(id);

		if (taskWrapper.isPresent()) {
			task = taskWrapper.get();
			logger.info("Task has been found using id", task);
			task.setStatus(status.toUpperCase());
			logger.debug("Status for the task has been updated",task.toString());
			if (status.equalsIgnoreCase(StatusEnum.STARTED.toString())) {
				LocalDate date = LocalDate.now();
				task.setDateStarted(date);
				logger.info("Time stamp when the task is moved to STARTED status has been captured", task.getDateStarted() );;
				Period period = Period.between(task.getDateCreated(), date);
				task.setTimeinCreatedstatus(period.getDays());
				logger.info("Tracked time the task has spent in CREATED status", task.getTimeinCreatedstatus());
			} else if (status.equalsIgnoreCase(StatusEnum.COMPLETED.toString())) {
				LocalDate date = LocalDate.now();
				task.setDateCompleted(date);
				logger.info("Time stamp when the task is moved to COMPLETED status has been captured", task.getDateCompleted());
				Period period = Period.between(task.getDateStarted(), date);
				task.setTimeInDateStartedstatus(period.getDays());
				logger.info("Tracked time the task has spent in CREATED status", task.getTimeInDateStartedstatus());
				if(task.isRecurringTask()){
					//calculate startdatefor the next recurring task
					logger.info("Task has been identified as a recurring task", task.isRecurringTask());
					LocalDate currentTaskCreationDate = task.getDateCreated();
					LocalDate newStartDate = currentTaskCreationDate.plusDays(task.getFrequency());
					logger.info("Setting the dateCreated for the new task");
					Task nextRecurringTask = (Task) task.clone();
					logger.info("new task has been cloned from the old task");
					nextRecurringTask.setDateCreated(newStartDate);
					nextRecurringTask.setDateStarted(null);
					nextRecurringTask.setDateCompleted(null);
					nextRecurringTask.setStatus(StatusEnum.CREATED.toString());
					nextRecurringTask.setTaskID(null); // mongo will create and assign new id 
					taskRepo.save(nextRecurringTask); //this can cause an exception
					logger.info("Saved the newly created recurring task to DB",nextRecurringTask.toString());
				}
			}
			taskRepo.save(task); //this can cause an excetion
		} else {
			// log error that task is null
		}
		return new ResponseEntity<Task>(task,HttpStatus.OK);
		}catch(Exception e){
			
		}

		return new ResponseEntity<Task>(task,HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/deletetask/{id}/" , method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteTask(@PathVariable(value="id") String id){
		
		try{
		if(taskRepo.existsById(id)){
		taskRepo.deleteById(id);
		logger.info("Task found in the repo", id);
		logger.debug("Task with the given id has been deleted", id);
		return new ResponseEntity<String>("Task deleted successfully",HttpStatus.OK);
		}
		else{
		return new ResponseEntity<String>("task not found",HttpStatus.OK);
		}
		}
		catch(Exception e){
		logger.error("Task deletion has failed", taskRepo.findById(id));
		}
		return new ResponseEntity<String>("Task deletion has failed",HttpStatus.BAD_REQUEST);
				
	}
	
}
