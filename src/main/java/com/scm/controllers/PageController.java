package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

//import ch.qos.logback.core.model.Model;

@Controller
public class PageController {
	@Autowired
	private UserService userService;
	@GetMapping("/")
	public String index()
	{
		return "redirect/home";
		
	}
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
    public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult, HttpSession session)
    {
    	System.out.println("Processing registration");
    	
//    	fetch form data
    	  //userForm
    	System.out.println(userForm);
    	
//    	validate form data
    	if (rBindingResult.hasErrors()) {
    		return "register";
			
		}
//    	save to database
    	
    	//userService 
    	//userForm cha data user madhye fetch kela aahe 
//    	User user= User.builder()
//    			.name(userForm.getName())
//    			.email(userForm.getEmail())
//    			.password(userForm.getPassword())
//    			.about(userForm.getAbout())
//    			.phoneNumber(userForm.getPhoneNumber())
//    			.profilePic("https://commons.wikimedia.org/wiki/File:Default_pfp.svg")
//    			.build();
    	User user=new User();
    	user.setName(userForm.getName());
    	user.setEmail(userForm.getEmail());
    	user.setPassword(userForm.getPassword());
    	user.setAbout(userForm.getAbout());
    	user.setPhoneNumber(userForm.getPhoneNumber());
    	user.setProfilePic("https://commons.wikimedia.org/wiki/File:Default_pfp.svg");
    	     User savedUser=  userService.saveUser(user);
    	     System.out.println("user saved :");

    	     //    	message="Registration Successful"

    	     //    	add the message
    	     Message message=Message.builder().content("Registration Successful").type(MessageType.green).build();
    	     
    	     session.setAttribute("message",message);

    	     //    	redirect to login page
    	
    	return "redirect:/register";
    }
	    
	   
	
}
