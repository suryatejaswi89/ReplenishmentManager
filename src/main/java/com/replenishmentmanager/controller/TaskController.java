package com.replenishmentmanager.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	
	@RequestMapping(value = "/task", method = RequestMethod.POST)
	@ResponseBody
	public Task createTask(@RequestBody Task task) {
		task.setStatus(StatusEnum.CREATED.toString());
		task.setWeightage(task.getPriority()*1000/task.getEstimate());
		return taskService.saveTask(task);
	}
	
	@RequestMapping(value="/{status}/", method = RequestMethod.GET)
	@ResponseBody
	public List<Task> getTasksbystatus(@PathVariable(value = "status") String status){
		List<Task> tasks = taskRepo.findAllBystatus(status);
		return tasks;
	}
	
	@RequestMapping(value = "/pendingtasks/", method = RequestMethod.GET)
	@ResponseBody
	public List<Task> pendingTasks(){
		
		List<Task> pendingTasks = new ArrayList<Task>();
		pendingTasks.addAll(taskRepo.findAllBystatus(StatusEnum.CREATED.toString()));
		pendingTasks.addAll(taskRepo.findAllBystatus(StatusEnum.STARTED.toString()));
	
		List<Task> sortedTasks = pendingTasks.stream()
				.sorted(Comparator.comparingDouble(Task::getWeightage).reversed())
				.collect(Collectors.toList());
		
		return sortedTasks;
		
	}
	
	@RequestMapping(value="/tasks/{id}/", method = RequestMethod.GET)
	@ResponseBody
	public Optional<Task> getTasksbyID(@PathVariable(value = "id") String id){
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
	public Task updateStatus(@PathVariable(value = "id") String id, @PathVariable(value = "status") String status) throws CloneNotSupportedException {
		Task task = null;
		Optional<Task> taskWrapper = taskRepo.findById(id);
		if (taskWrapper.isPresent()) {
			task = taskWrapper.get();
			task.setStatus(status.toUpperCase());
			if (status.equalsIgnoreCase(StatusEnum.STARTED.toString())) {
				LocalDate date = LocalDate.now();
				task.setDateStarted(date);
				Period period = Period.between(task.getDateCreated(), date);
				task.setTimeinCreatedstatus(period.getDays());
			} else if (status.equalsIgnoreCase(StatusEnum.COMPLETED.toString())) {
				LocalDate date = LocalDate.now();
				task.setDateCompleted(date);
				Period period = Period.between(task.getDateStarted(), date);
				task.setTimeInDateStartedstatus(period.getDays());
				if(task.isRecurringTask()){
					//calculate startdatefor the next recurring task
					LocalDate currentTaskCreationDate = task.getDateCreated();
					System.out.println(task.getDateCreated().toString());
					LocalDate newStartDate = currentTaskCreationDate.plusDays(task.getFrequency());
					System.out.println(task.getDateCreated().toString());
					Task nextRecurringTask = (Task) task.clone();
					nextRecurringTask.setDateCreated(newStartDate);
					nextRecurringTask.setStatus(StatusEnum.CREATED.toString());
					nextRecurringTask.setTaskID(null); // mongo will create and assign new id 
					taskRepo.save(nextRecurringTask);
				}
			}
			taskRepo.save(task);
		} else {
			// log error that task is null
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
