package com.replenishmentmanager.service;

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
import org.springframework.stereotype.Service;

import com.replenishmentmanager.commons.StatusEnum;
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
	
	final static Logger logger = LogManager.getLogger();
	
	
	/**
	 * returns task that is saved to the database
	 * @param task task whose status and weightage should be set and saved to the database
	 * @return     the task in JSON format
	 */
	public Task createTask(Task task){
		task.setStatus(StatusEnum.CREATED.toString());
		task.setWeightage(task.getPriority()*1000/task.getEstimate());
		return taskRepo.save(task);
	}
	
	/**
	 * This method will return the list of the tasks from the database whose status matches
	 * with the given status
	 * @param status A string with which the status of the tasks in database should
	 * be matched
	 * @return List  The lists of the tasks with specified status
	 */
	public List<Task> getTaskbystatus(String status){
		 List<Task> tasks = taskRepo.findAllBystatus(status);
		return tasks;
	}

	/**
	 * returns the Task with the specified id from the database
	 * @param id String with which the taskID of each task should be matched
	 * @return   returns a task object in JSON format
	 */
	public Optional<Task> getTaskbyID(String id){
		Optional<Task> task=null;
				if(taskRepo.existsById(id)){
					task = taskRepo.findById(id);
				}		
		return task;
	}
	/**
	 * returns list of all the tasks from the database
	 * @return 		list of tasks in JSON format
	 */
	public List<Task> getAllTasks(){
		List<Task> tasks = taskRepo.findAll();
		return tasks;
	}
	
	/**
	 * returns list of tasks based on the name given in the arguments.This string
	 * should be should be used to fetch the userID and this is used to fetch the task
	 * from the tasks collection by matching against the taskOwnerID
	 * @param name String with which task is found based on the taskOwnerID
	 * @return		List of tasks created by the user in JSON format
	 */
	public List<Task> getTasksbyCreator(String name){
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		List<Task> tasks = taskRepo.findBytaskOwnerID(id);
		return tasks;
	}

	/**
	 * returns returns list of tasks based on the name given in the arguments.This string
	 * should be should be used to fetch the userID and this is used to fetch the task
	 * from the tasks collection by matching against the AssigneeID
	 * @param name String with which task is found based on the AssigneeID
	 * @return     List of tasks assigned to the user in JSON format
	 */
	public List<Task> getTasksAssigned(String name){
		User user = userRepo.findByfName(name);
		String id = user.getUserId();
		List<Task> tasks = taskRepo.findByassigneeID(id);
		return tasks;
	}
	
	/**
	 * returns list of the tasks whose status is pending(that includes both the created 
	 * and started status)
	 * @return   List of the status which are in created and started statuses(not completed)
	 */
	public List<Task> getPendingTasks(){
		List<Task> pendingTasks = new ArrayList<Task>();
		pendingTasks.addAll(taskRepo.findAllBystatus(StatusEnum.CREATED.toString()));
		pendingTasks.addAll(taskRepo.findAllBystatus(StatusEnum.STARTED.toString()));
	
		List<Task> sortedTasks = pendingTasks.stream()
				.sorted(Comparator.comparingDouble(Task::getWeightage).reversed())
				.collect(Collectors.toList());
		logger.info("Pending tasks have been listed successfully",sortedTasks);
		logger.debug("Pending tasks have been listed successfully",sortedTasks.toString());
		return sortedTasks;
	}
	
	//Method to update the status of the task by the given id.
	/**
	 * returns the task after updating the task with the status the user mentioned. To update the 
	 * status of the task first its found in the database using the task id
	 * @param id String which is used to find the task whose status should be updated
	 * @param status String with which the status field of the retrieved task should be updated
	 * @return		Task with the updated status
	 * @throws Exception used to throw an exception when there is no task with the given id in the db
	 */
	public Task updateStatus(String id, String status) throws Exception{
		Task task = null;
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
					createRecurringTask(task);
				}
			}
			task = taskRepo.save(task); //this can cause an exception
		} else {
			//Throw an exception to let controller know that finding task db query failed
			throw new Exception("Task Not found");
		}
		return task;
	}
	
		//method to create a recurring task.
		// handling exceptions for recurring task creation in service layer because the response of the updatestatus is not effected
		// by the outcome of the creating recurring task.
	/**
	 * returns a recurringTask that has been created for a task whose isRecurring is true
	 * once its completed. All the fields are set for the recurringTask created and its returned 
	 * @param task Task for which a new recurringTask has to be created
	 */
	public void createRecurringTask(Task task) {
		try {
			LocalDate currentTaskCreationDate = task.getDateCreated();
			LocalDate newStartDate = currentTaskCreationDate.plusDays(task.getFrequency());
			logger.info("Setting the dateCreated for the new task");
			Task nextRecurringTask = (Task) task.clone(); //might throw CloneNotSupportedException
			logger.info("new task has been cloned from the old task");
			nextRecurringTask.setDateCreated(newStartDate);
			nextRecurringTask.setDateStarted(null);
			nextRecurringTask.setDateCompleted(null);
			nextRecurringTask.setStatus(StatusEnum.CREATED.toString());
			nextRecurringTask.setTaskID(null); // mongo will create and assign
												// new id
			taskRepo.save(nextRecurringTask); // might throw an exception here
			logger.info("Created a recurring task successfully: {}", nextRecurringTask.getTaskID());
			logger.debug("Created a recurring task successfully with the following details: {}", nextRecurringTask.toString());
		} catch (CloneNotSupportedException e) {
			logger.error("Create recurrring task failed with error: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Failed to create a recurring task", task);
		}
	}
	
	/**
	 * returns the message "suceesfully deleted" if it is able to find the task with the
	 * given id and removes it from the db. else it returns task doesn't exists if it could not 
	 * find the task with the given id
	 * @param id String which is used to find the task in the db.
	 * @return 	 String based on the action occurred
	 * @throws Exception throws a new exception to say that the task is not found.
	 */
	public String deleteTask(String id) throws Exception {
		if (taskRepo.existsById(id)) {
			taskRepo.deleteById(id);
			return "Succesfully deleted";
		} else {
			throw new Exception("Task doesn't exists");
		}
	}
}