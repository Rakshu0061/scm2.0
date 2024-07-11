package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import com.scm.forms.UserForm;

//import ch.qos.logback.core.model.Model;

@Controller
public class PageController {
	
   //@RequestMapping("/home")
	@GetMapping("/home")
   public String home() {
		return "home";
	
}
	@GetMapping("/about")
	public String aboutPage()
	{
		System.out.println("About page loading");
		return "about";
	}
	@GetMapping("/services")
	public String servicesPage()
	{
		System.out.println("Service page loading");
		return "services";
	}
	 @GetMapping("/contact")
	    public String contact() {
	        return new String("contact");
	    }
	 @GetMapping("/login")
	    public String login() {
	        return new String("login");
	    }
	 @GetMapping("/register")
	 public String register(Model model) //passing data from database to view
	 { 
		 UserForm userForm=new UserForm();
		 model.addAttribute("userForm",userForm);
		 
		 return "register";
	 }
//	 processing register
    @RequestMapping(value="/do-register", method= RequestMethod.POST)
    public String processRegister(@ModelAttribute UserForm userForm)
    {
    	System.out.println("Processing registration");
//    	fetch form data
//    	validate form data
//    	save to database
//    	message="Registration Successful"
//    	redirect to login page
    	
    	return "redirect:/register";
    }
	    
	   
	
}
