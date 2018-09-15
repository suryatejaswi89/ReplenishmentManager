package com.replenishmentmanager.controller;



import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping(value = "/createuser", method = RequestMethod.POST)
	@ResponseBody
	public User createUser(@RequestBody User user) {
		String fname = user.getfName();
		String lName = user.getlName();
		String designation = user.getDesignation();
		return userService.createUser(fname,lName,designation);
	}

}
