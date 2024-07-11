package com.scm.entities;

import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
//import jakarta.validation.constraints.AssertFalse.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Entity(name="user")
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
     @Id
	private String userId;
     @Column(name="user_name",nullable=false)
	private String name;
     
     @Column(unique = true,nullable = false)
	private String email;
     
	private String password;
	
	@Column(length = 1000)
	private String about;
	
	@Column(length=1000)
	private String profilePic;
	
	private String phoneNumber;
	//user verification
	private boolean enabled=false;
	private boolean emailVerified= false;
	private boolean phoneVerified=false;
//	login by self, google,facebook,twitter,linkedin,github
	private Providers provider=Providers.SELF;
	private String providerUserId;
	
//	add more fields if needed
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
	private List<Contact> contacts = new ArrayList<>();
	
	
}
