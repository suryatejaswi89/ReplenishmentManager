package com.replenishmentmanager.model;

import org.springframework.data.annotation.Id;


public class User {
	
	@Id
	public String userId;
	public String fName;
	public String lName;
	public String designation;
	/**
	 * @param userId
	 * @param fName
	 * @param lName
	 * @param designation
	 */
	public User(String userId,String fName, String lName, String designation) {
		this.userId = userId;
		this.fName = fName;
		this.lName = lName;
		this.designation = designation;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}
	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
	}
	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @param fName the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}
	/**
	 * @param lName the lName to set
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}
	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userId=" + userId + ", fName=" + fName + ", lName=" + lName + ", designation=" + designation
				+ "]";
	}
	
	

}
