package com.scm.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;

@Repository //repository to interact with database
public interface UserRepo extends JpaRepository<User, String> {
//extra methods for db related operations
// custom query methods
//custom finder methods
	
	//custom finder
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPassword(String email,String password);
}
