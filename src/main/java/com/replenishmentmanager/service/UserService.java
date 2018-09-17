package com.replenishmentmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.replenishmentmanager.model.User;
import com.replenishmentmanager.model.UserRepository;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	/**
	 * this method returns the user object in JSON format after creating it
	 * and persisting to the users collection.
	 * @param fName string which should be assigned as user's fname
	 * @param lName String which should be assigned as user's lname
	 * @param designation string which should be assigned as user's designation
	 * @return       this method  returns the created and saved user in json format along with the  unique id given by mongodb
	 */
	public User createUser(String fName,String lName,String designation){
		User user = new User();
		user.setfName(fName);
		user.setlName(lName);
		user.setDesignation(designation);
		userRepo.save(user);
		return user;
		
	}
	
	
	
	
	
	
}
