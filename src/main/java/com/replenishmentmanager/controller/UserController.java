package com.replenishmentmanager.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.replenishmentmanager.model.User;
import com.replenishmentmanager.service.UserService;

@Controller
public class UserController {
	
	
	
	@Autowired
	private UserService userService;
	/**
	 * this method returns a new ResponseEntity along with the created user and a http response status.
	 * @param user User object that has to be saved in the repo.
	 * @return
	 */
	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User user1 = userService.createUser(user.fName, user.lName, user.designation); 
		return new ResponseEntity<User>(user1, HttpStatus.OK);
	}

}
