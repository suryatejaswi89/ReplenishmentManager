package com.replenishmentmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.replenishmentmanager.model.User;
import com.replenishmentmanager.model.UserRepository;


@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	public User createUser(String fName,String lName,String designation){
		User user = new User();
		user.setfName(fName);
		user.setlName(lName);
		user.setDesignation(designation);
		userRepo.save(user);
		return user;
		
	}
	
	
	
	
}
