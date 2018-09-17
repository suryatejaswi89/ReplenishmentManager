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

	/**
	 * returns a new responsEntity of type Task with a status after a task that
	 * has been passed as a requestbody is saved to the db.
	 * 
	 * @param task
	 *            Task which should be persisted to db
	 * @return ResponseEntity which returns the task created with a http status
	 */

	@RequestMapping(value = "/task", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		try {
			Task newTask = taskService.createTask(task);
			logger.info("Task with id {} got created successfully", newTask.getTaskID());
			logger.debug("Persisted task with id {} and with details {}", newTask.getTaskID(), newTask.toString());
			return new ResponseEntity<Task>(newTask, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Task creation failed with the exception: {}", e.getMessage());
			return new ResponseEntity<Task>(task, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * returns a list of the tasks in the given uri and also returns a http
	 * status message based on whether the request was successful /not
	 * 
	 * @param status
	 *            A string with which the tasks are retreived from the db
	 * @return returns a list of tasks based on the input status along with a
	 *         http status message
	 */
	@RequestMapping(value = "/{status}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getTasksbystatus(@PathVariable(value = "status") String status) {
		List<Task> tasks = null;
		try {
			tasks = taskService.getTaskbystatus(status);
			logger.info("Tasks with given status have been found succesfully", tasks);
			logger.debug("tasks with given status {}", tasks.toString());
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("fetching the task with given task has failed", e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.BAD_REQUEST);
	}

	/**
	 * returns all the tasks from the given uri who status is either created or
	 * started(as tasks in both status are considered to be pending) and http
	 * response status based on the success of the request
	 * 
	 * @return A new response entity with list of pending tasks and a http
	 *         response status.
	 */
	@RequestMapping(value = "/pendingtasks/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> pendingTasks() {
		List<Task> tasks = null;
		try {
			tasks = taskService.getPendingTasks();
			logger.info("Pending tasks have been returned", tasks);
			logger.debug("Pending tasks have been returned", tasks.toString());
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Failed to get pending tasks");
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.SERVICE_UNAVAILABLE);
	}

	/**
	 * returns a task based on the input id given by the user from the given uri
	 * along with a http response status
	 * 
	 * @param id String with which task is identified and retrieved from the db
	 *            
	 * @return A new ResponseEntity with a Task and http Status message.
	 */
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

	/**
	 * returns a list of all tasks from the db
	 * 
	 * @return new ResponseEntity with a list of all tasks and http response
	 *         status
	 */
	@RequestMapping(value = "/tasks/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getAllTasks() {
		List<Task> tasks = null;
		try {
			tasks = taskService.getAllTasks();
			logger.info("All tasks have been listed", tasks);
			logger.debug("All tasks have been listed", tasks.toString());
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Listing tasks has failed due to an error", e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.SERVICE_UNAVAILABLE);

	}

	/**
	 * returns a list of the tasks with created by the given name. Here given
	 * string is used to fetch the userID from the user collection which is used
	 * to match against the taskOwnerID in tasks collection and returns the
	 * matching tasks.If there is db connection error this will thrown an
	 * service_unaivalable error message.If there are no matching tasks it will
	 * return an empty list
	 * 
	 * @param name
	 *            String with which the userId should be fetched and matched
	 *            against the taskOwnerID
	 * @return ResponseEntity with a list of tasks created by the user along
	 *         with a http status message
	 */

	@RequestMapping(value = "/createdByUser/{name}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getAllTaskscreatedByUser(@PathVariable(value = "name") String name) {
		List<Task> tasks = null;
		try {
			tasks = taskService.getTasksbyCreator(name);
			logger.info("All tasks created by user with given id have been listed", tasks);
			logger.debug("All tasks have been listed", tasks.toString());
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Listing tasks by ownerId have failed", e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.BAD_REQUEST);
	}

	/**
	 * returns a list of the tasks with created by the given name. Here given
	 * string is used to fetch the userID from the user collection which is used
	 * to match against the AssigneeID in tasks collection and returns the
	 * matching tasks.If there is db connection error this will thrown an
	 * service_unaivalable error message.If there are no matching tasks it will
	 * return an empty list
	 * 
	 * @param name
	 *            String with which the userId should be fetched and matched
	 *            against the taskOwnerID
	 * @return ResponseEntity with a list of tasks created by the user along
	 *         with a http status message
	 */
	@RequestMapping(value = "/assignedToUser/{name}/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<Task>> getTasksAssigned(@PathVariable(value = "name") String name) {
		List<Task> tasks = null;
		try {
			tasks = taskService.getTasksAssigned(name);
			logger.info("All tasks assigned to user with given id have been listed", tasks);
			logger.debug("All tasks assigned to the have been listed", tasks.toString());
			return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Listing tasks by AssigneeID have failed", e.getMessage());
		}
		return new ResponseEntity<List<Task>>(tasks, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Returns the task with the new updated status. The given id is used to get
	 * the specific task from the db if it exists and then the given status is
	 * used to updated the existing status of the task and will return the task
	 * with updated task. When the new updated status of task is "COMPLETED"
	 * this will trigger a method to createRecurringTask if that task is a
	 * recurring task and then it is saved to the database with all the fields
	 * set with a new id.
	 * 
	 * @param id
	 *            String used to search for the task in the db
	 * @param status
	 *            String which should update the existing status
	 * @return A new ResponseEntity with the updated task and http status
	 *         message
	 * @throws CloneNotSupportedException
	 *             This exception is thrown by this method because we are
	 *             cloning the task if its a recurringtask when its status is
	 *             "COMPLETED"
	 */
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

	/**
	 * this methods returns a string with a message whether the specified task
	 * is deleted or not. The given input string is used to fetch the task and
	 * delete it from the db.
	 * 
	 * @param id
	 *            String which is used to identify the task in the database.
	 * @return new ResponseEntity with string message and a http status message
	 */
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
