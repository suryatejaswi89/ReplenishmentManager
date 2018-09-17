package com.replenishmentmanager.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.replenishmentmanager.model.Task;
import com.replenishmentmanager.model.TaskRepository;
import com.replenishmentmanager.model.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {
	
	@MockBean
	private TaskRepository taskRepository;
	
	@MockBean
	private UserRepository userRepo;
	
	@Autowired
	private TaskService taskService;
	
	LocalDate createdDate = LocalDate.now();
	
	Task mockTask = new Task("5b9e7f2e9e58662c28581e4f", "Place an order for bananas",
			"5b9d2bd0173edc52dfedadec", "5b9d2be5173edc52dfedadee", createdDate, "created", 2, 1, 2.0);

	@Test
	public void test_createTask_success() {
		Mockito.when(taskRepository.save(Mockito.any())).thenReturn(mockTask);
		Task createdTask=taskService.createTask(mockTask);
		assertEquals(createdTask.getTaskID(),"5b9e7f2e9e58662c28581e4f");
	}
	
	@Test
	public void test_updateStatus_create_next_recurring_task() throws Exception{
		Task mockTask = new Task("5b9e7f2e9e58662c28581e4f", "Place an order for bananas",
				"5b9d2bd0173edc52dfedadec", "5b9d2be5173edc52dfedadee", createdDate, "STARTED", 2, 1, 2.0);
		mockTask.setRecurringTask(true);
		mockTask.setFrequency(3);
		mockTask.setDateStarted(LocalDate.now().minusDays(2));
		Optional<Task> optionalMockTask = Optional.of(mockTask);
		Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(optionalMockTask);
		Task updatedTask = taskService.updateStatus("5b9e7f2e9e58662c28581e4f", "COMPLETED");
		//first call to save is to create a next recurring task and second call to save is to save the updated task to db
		Mockito.verify(taskRepository, Mockito.times(2)).save(Mockito.any());
	}
}
