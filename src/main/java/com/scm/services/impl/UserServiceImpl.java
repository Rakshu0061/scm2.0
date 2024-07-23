package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repository.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {
 @Autowired
	private UserRepo userRepo;
 @Autowired
 private PasswordEncoder passwordEncoder;
 
 private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Override
	public User saveUser(User user) {
		//user id: have to genarete
		String userId =UUID.randomUUID().toString();
		user.setUserId(userId);
		// password encode and set the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		//set the user role
		user.setRoleList(List.of(AppConstants.ROLE_USER));
		return userRepo.save(user);
	}

	@Override
	public Optional<User> getUserById(String id) {
		// TODO Auto-generated method stub
		return userRepo.findById(id);
	}

	@Override
	public Optional<User> updateUser(User user) {
		// get user
		User user2=	userRepo.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		//we will update user2 from user 
		 user2.setName(user.getName());
		 user2.setEmail(user.getEmail());
		 user2.setPassword(user.getPassword());
		 user2.setAbout(user.getAbout());
		 user2.setPhoneNumber(user.getPhoneNumber());
		 user2.setProfilePic(user.getProfilePic());
		 user2.setEnabled(user.isEnabled());
		 user2.setEmailVerified(user.isEmailVerified());
		 user2.setPhoneVerified(user.isPhoneVerified());
		 user2.setProvider(user.getProvider());
		 user2.setProviderUserId(user.getProviderUserId());
		  
		 //save the user in database
		 User save = userRepo.save(user2);
		 
		return Optional.ofNullable(save);
	}

	@Override
	public void deleteUser(String id) {
		//fetch the user to delete by id
		User user2 = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("user not found"));
		userRepo.delete(user2);
		
	}

	@Override
	public boolean isUserExists(String userId) {
		User user2= userRepo.findById(userId).orElse(null);
		return user2 !=null ? true : false;
	}

	@Override
	public boolean isUserExistsByEmail(String email) {
	User user2= userRepo.findByEmail(email).orElse(null);
		return user2 != null ? true:false ;
	}

	@Override
	public List<User> getAllUsers() {
		
		return userRepo.findAll();
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepo.findByEmail(email).orElseThrow(null);
	}

	
}
