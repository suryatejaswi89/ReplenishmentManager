package com.replenishmentmanager.model;

import java.time.LocalDate;
import java.util.Date;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="tasks")
public class Task {
	
	//cleanup make vars private 
	
	@Id
	public String taskID;
	/**
	 * @return the weightage
	 */
	public double getWeightage() {
		return weightage;
	}
	/**
	 * @param weightage the weightage to set
	 */
	public void setWeightage(double weightage) {
		this.weightage = weightage;
	}
	/**
	 * @param date the dateCompleted to set
	 */
	public void setDateCompleted(LocalDate date) {
		this.dateCompleted = date;
	}
	/**
	 * @param date the inProgress to set
	 */
	public void setInProgress(LocalDate date) {
		this.inProgress = date;
	}
	public String description;
	public String taskOwnerID;
	public String assigneeID;
	public LocalDate dateCreated;
	public String status;
	public int priority;
	/*
	  no:of days estimated to complete the task
	 */
	public int estimate;
	public double weightage;
	public LocalDate dateCompleted;
	public LocalDate inProgress;
	private int timeinCreatedstatus;
	private int timeininProgressstatus;
	
	
	/**
	 * @return the timeinCreatedstatus
	 */
	public int getTimeinCreatedstatus() {
		return timeinCreatedstatus;
	}
	/**
	 * @param timeinCreatedstatus the timeinCreatedstatus to set
	 */
	public void setTimeinCreatedstatus(int timeinCreatedstatus) {
		this.timeinCreatedstatus = timeinCreatedstatus;
	}
	/**
	 * @return the timeininProgressstatus
	 */
	public int getTimeininProgressstatus() {
		return timeininProgressstatus;
	}
	/**
	 * @param timeininProgressstatus the timeininProgressstatus to set
	 */
	public void setTimeininProgressstatus(int timeininProgressstatus) {
		this.timeininProgressstatus = timeininProgressstatus;
	}
	/**constructor
	 * @param taskID
	 * @param description
	 * @param taskOwnerID
	 * @param assigneeID
	 * @param dateCreated
	 * @param status
	 * @param estimate
	 */
	public Task(String taskID, String description, String taskOwnerID, String assigneeID, LocalDate dateCreated,
			String status,int priority, int estimate, double weightage) {
		super();
		this.taskID = taskID;
		this.description = description;
		this.taskOwnerID = taskOwnerID;
		this.assigneeID = assigneeID;
		this.dateCreated = dateCreated;
		this.status = status;
		this.priority = priority;
		this.estimate = estimate;
		this.weightage = weightage;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * 
	 */
	public Task() {
		
	}
	/**
	 * @return the taskID
	 */
	public String getTaskID() {
		return taskID;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the taskOwnerID
	 */
	public String getTaskOwnerID() {
		return taskOwnerID;
	}
	/**
	 * @return the assigneeID
	 */
	public String getAssigneeID() {
		return assigneeID;
	}
	/**
	 * @return the dateCreated
	 */
	public LocalDate getDateCreated() {
		return dateCreated;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @return the estimate
	 */
	public int getEstimate() {
		return estimate;
	}
	/**
	 * @return the dateCompleted
	 */
	public LocalDate getDateCompleted() {
		return dateCompleted;
	}
	/**
	 * @return the inProgress
	 */
	public LocalDate getInProgress() {
		return inProgress;
	}
	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @param taskOwnerID the taskOwnerID to set
	 */
	public void setTaskOwnerID(String taskOwnerID) {
		this.taskOwnerID = taskOwnerID;
	}
	/**
	 * @param assigneeID the assigneeID to set
	 */
	public void setAssigneeID(String assigneeID) {
		this.assigneeID = assigneeID;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @param estimate the estimate to set
	 */
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Task [taskID=" + taskID + ", description=" + description + ", taskOwnerID=" + taskOwnerID
				+ ", assigneeID=" + assigneeID + ", dateCreated=" + dateCreated + ", status=" + status + ", estimate="
				+ estimate + "]";
	}
	
	
	
	

}
