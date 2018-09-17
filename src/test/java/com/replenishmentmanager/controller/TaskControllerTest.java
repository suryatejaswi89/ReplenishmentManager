package com.replenishmentmanager.controller;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.replenishmentmanager.model.Task;
import com.replenishmentmanager.model.TaskRepository;
import com.replenishmentmanager.service.TaskService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TaskController.class, secure = false)
public class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@MockBean
	private TaskRepository taskRepository;

	LocalDate createdDate = LocalDate.now();
	Task mockTask = new Task("5b9e7f2e9e58662c28581e4f", "Place an order for bananas",
			"5b9d2bd0173edc52dfedadec", "5b9d2be5173edc52dfedadee", createdDate, "created", 2, 1, 2.0);

	@Test
	public void test_getTasksbyID_success() throws Exception {
		Optional<Task> mockOptionalTask = Optional.of(mockTask);
		Mockito.when(taskService.getTaskbyID(Mockito.anyString())).thenReturn(mockOptionalTask);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks/5b9e7f2e9e58662c28581e4f/")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(), 200);
	}
	@Test
	public void test_getTasksbyID_failure() throws Exception {
		Mockito.when(taskService.getTaskbyID(Mockito.anyString())).thenThrow(new RuntimeException());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks/5b9e7f2e9e58662c28581e4f/")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(), 503);
	}
	
	@Test
	public void test_getAllTasks_success() throws Exception{
		List<Task> mockTaskList = new ArrayList<Task>();
		mockTaskList.add(mockTask);
		Mockito.when(taskService.getAllTasks()).thenReturn(mockTaskList);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks/")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(),200);
		
	}
	
	@Test
	public void test_getAllTasks_failure() throws Exception{
		List<Task> mockTaskList = new ArrayList<Task>();
		mockTaskList.add(mockTask);
		Mockito.when(taskService.getAllTasks()).thenThrow(new RuntimeException());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tasks/")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(),503);
	}
	
	@Test
	public void test_updateStatus_success() throws Exception{
		
		Mockito.when(taskService.updateStatus(Mockito.anyString(), Mockito.anyString())).thenReturn(mockTask);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateStatus/5b9e7f2e9e58662c28581e4f/started/")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(),200);
		
	}
	
	@Test
	public void test_updateStatus_failure() throws Exception{
		Mockito.when(taskService.updateStatus(Mockito.anyString(), Mockito.anyString())).thenThrow(new RuntimeException());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateStatus/5b9e7f2e9e58662c28581e4f/started/")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(),503);
	}
	
	@Test
	public void test_deleteTask_success() throws Exception{
		String response = "succesfully deleted";
		Mockito.when(taskService.deleteTask(Mockito.anyString())).thenReturn(response);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/deletetask/5b9e7f2e9e58662c28581e4f/")
				.accept(MediaType.ALL);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(),200);
		
	}
	@Test
	public void test_deleteTask_failure() throws Exception{
		Mockito.when(taskService.deleteTask(Mockito.anyString())).thenThrow(new RuntimeException());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/deletetask/5b9e7f2e9e58662c28581e4f/")
				.accept(MediaType.ALL);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(result.getResponse().getStatus(),503);
		
	}
	
}
