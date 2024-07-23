/* user se related jitne bhi protected urls hain,unke related jitne bji handlers honge unhe UserController main likhana hain*/
package com.scm.controllers;



import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.services.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
	private Logger logger = LoggerFactory.getLogger(UserController.class);
//user dashboard page
	private UserService userService;
	@RequestMapping(value="/dashboard")
	public String userDashboard()
	{  
		 System.out.println("User dashboard");
		 return "user/dashboard";
	}
	//user profile
	@RequestMapping(value="/profile")
	public String userProfile( Authentication authentication)
	{
         
	 return "user/profile";
	}
	
	
	// user add contacts page
	//user view contacts
	
}
