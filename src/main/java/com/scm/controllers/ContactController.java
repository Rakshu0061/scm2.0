package com.scm.controllers;


import java.util.List;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	private Logger logger= LoggerFactory.getLogger(ContactController.class);
	@Autowired
	private ContactService contactService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private UserService userService;
	
	//add contact page:handler
	@RequestMapping("/add")
	public String addContactView(Model model)
	{ 
		ContactForm contactForm=new ContactForm();
		contactForm.setFavorite(true);
		model.addAttribute("contactForm",contactForm);
		return "user/add_contact";
	}
	
	@RequestMapping(value="/add",method= RequestMethod.POST)
	public String saveContact(@Valid  @ModelAttribute ContactForm contactForm,BindingResult rBindingResult, Authentication authentication,HttpSession session)
	{
		if(rBindingResult.hasErrors())
		{
			
			return "user/add_contact";
			
		}
		String username= Helper.getEmailOfLoggedInUser(authentication);
		//process form data
		User user= userService.getUserByEmail(username);
		
		//image process
		String filename=UUID.randomUUID().toString();
		String fileurl= imageService.uploadImage(contactForm.getContactImage(),filename);
		Contact contact=new Contact();//convert form to contact
		System.out.println("PhoneNumber="+contactForm.getPhoneNumber());
		 contact.setName(contactForm.getName());
		 contact.setFavorite(contactForm.isFavorite());
		 contact.setEmail(contactForm.getEmail());
		 contact.setPhoneNumber(contactForm.getPhoneNumber());
		 contact.setAddress(contactForm.getAddress());
		 contact.setDescription(contactForm.getDescription());
		 contact.setUser(user);
		 contact.setLinkedInLink(contactForm.getLinkedInLink());
		 contact.setWebsiteLink(contactForm.getWebsiteLink());
		 contact.setPicture(fileurl);
		 contact.setCloudinaryImagePublicId(filename);
		 
	      contactService.save(contact);
		
		System.out.println(contactForm);
	
		session.setAttribute("message",
				Message.builder().content("You have successfully added a new contact")
		        .type(MessageType.green)
		        .build());
		
		return "redirect:/user/contacts/add";
		
	}
	//view contacts
	@RequestMapping
	public String viewContacts(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,			
            Model model ,Authentication authentication)
            
			{
	String username= Helper.getEmailOfLoggedInUser(authentication);
	User user=userService.getUserByEmail(username);
		Page<Contact> pagecontacts=contactService.getByUser(user,page,size ,sortBy,direction);
		model.addAttribute("pagecontacts",pagecontacts);
		model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
		return "user/contacts";
		
	}
	//search user
	
	
	//search handler
	@RequestMapping("/search")
	public String searchHandler(
			@RequestParam("field") String field,
			@RequestParam("keyword") String value,
			@RequestParam(value  = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,Authentication authentication)
	{
		 logger.info("field {} keyword {}", field,value);
		   var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
		 Page<Contact> pagecontacts = null;
		 if(field.equalsIgnoreCase("name"))
		 {
			pagecontacts=contactService.searchByName(value, size, page, sortBy, direction,user);
			 
			 
		 }
		 else if (field.equalsIgnoreCase("email")) {
			 
			 pagecontacts=contactService.searchByEmail(value, size, page, sortBy, direction,user);
		}
		 else if(field.equalsIgnoreCase("phone")){
			 pagecontacts=contactService.searchByPhoneNumber(value, size, page, sortBy, direction,user);
		}
		  logger.info("pagecontacts {}", pagecontacts);
		  model.addAttribute("pagecontacts", pagecontacts);
		  model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
		 return "user/search";
	}
	
	//delete contact
	
	@RequestMapping("/delete/{contactId}")
	public String deleteContact(@PathVariable("contactId") String contactId,HttpSession session)
	{
		contactService.delete(contactId);
		
		logger.info("contactId {} deleted",contactId);
		// logger.info("contactId",contactId);
		 session.setAttribute("message",
	                Message.builder()
	                        .content("Contact is Deleted successfully !! ")
	                        .type(MessageType.green)
	                        .build()

	        );
		return "redirect:/user/contacts";
	}
	
	//update contact view
	@RequestMapping("/view/{contactId}")
	public String updateContactFormView(@PathVariable("contactId")String contactId, Model model)
	{
		 var contact = contactService.getById(contactId);
		 
	        ContactForm contactForm = new ContactForm();
	        contactForm.setName(contact.getName());
	        contactForm.setEmail(contact.getEmail());
	        contactForm.setPhoneNumber(contact.getPhoneNumber());
	        contactForm.setAddress(contact.getAddress());
	        contactForm.setDescription(contact.getDescription());
	        contactForm.setFavorite(contact.isFavorite());
	        contactForm.setWebsiteLink(contact.getWebsiteLink());
	        contactForm.setLinkedInLink(contact.getLinkedInLink());
	        contactForm.setPicture(contact.getPicture());
	        
	        model.addAttribute("contactForm", contactForm);
	        model.addAttribute("contactId", contactId);
	        
		return "user/update_contact_view";
	}
	
	//update user
	@RequestMapping(value = "/update/{contactId}",method = RequestMethod.POST)
	public String updateContact(@PathVariable("contactId") String contactId,
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            Model model)
	{
		
		//update contact
		  if (bindingResult.hasErrors()) {
	            return "user/update_contact_view";
	        }
		
		 var con = contactService.getById(contactId);
	        con.setId(contactId);
	        con.setName(contactForm.getName());
	        con.setEmail(contactForm.getEmail());
	        con.setPhoneNumber(contactForm.getPhoneNumber());
	        con.setAddress(contactForm.getAddress());
	        con.setDescription(contactForm.getDescription());
	        con.setFavorite(contactForm.isFavorite());
	        con.setWebsiteLink(contactForm.getWebsiteLink());
	        con.setLinkedInLink(contactForm.getLinkedInLink());
	        
	        // process image:

	        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
	            logger.info("file is not empty");
	            String fileName = UUID.randomUUID().toString();
	            String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
	            con.setCloudinaryImagePublicId(fileName);
	            con.setPicture(imageUrl);
	            contactForm.setPicture(imageUrl);

	        } else {
	            logger.info("file is empty");
	        }

	        var updateCon = contactService.update(con);
	        logger.info("updated contact {}", updateCon);

	        model.addAttribute("message", Message.builder().content("Contact Updated !!").type(MessageType.green).build());

		return "redirect:/user/contacts/view/" + contactId;
		
	}
	
}
