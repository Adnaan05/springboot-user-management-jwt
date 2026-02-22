package com.addy.spring_project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addy.spring_project.entities.UserEntity;
import com.addy.spring_project.exceptions.ResourceNotFoundException;
import com.addy.spring_project.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserRepository userRep;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping
	public List<UserEntity> getUsersList() {
		System.out.println("Request : GET Users List");
		return userRep.findAll();
	}
	
	@PostMapping
	public UserEntity createUsers( @RequestBody UserEntity user ) {
		System.out.println("Added User data:" + "\n" + "Name : " + user.getName() + "\n" + "Email : " + user.getEmail());
		
		user.setPassword(passwordEncoder.encode(user.getPassword() ) );
		return userRep.save(user);
		
	}
	
	
	@GetMapping("/{id}")
	public UserEntity GetSingleUser( @PathVariable Long id ) {
		Optional<UserEntity> userOptional = userRep.findById(id);
		if(userOptional.isPresent()) {
			UserEntity user = userOptional.get();
			System.out.println("Requested user id : " + user.getId());
			return user;
		}
		else {
			System.out.println("Requested invalid user");
			throw new ResourceNotFoundException("User not found with id: " + id);
		}

	}
	
	
	@PutMapping("/{id}")
	public UserEntity UpdateUser( @PathVariable Long id, @RequestBody UserEntity newData ) {
		UserEntity userData = userRep.findById(id).orElseThrow( () -> new  ResourceNotFoundException("User not found with id: " + id));
		System.out.println("Requested update for id : " + id);
		System.out.println("Old Data : " + "\n" + "Name : " + userData.getName() + "\n" + "Email : " + userData.getEmail() );
		
		userData.setName(newData.getName());
		userData.setEmail(newData.getEmail());
		
		System.out.println("New Data : " + "\n" + "Name : " + userData.getName() + "\n" + "Email : " + userData.getEmail() );
		return userRep.save(userData);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> DeleteUser(@PathVariable Long id) {
		UserEntity userData = userRep.findById(id).orElseThrow( () -> new  ResourceNotFoundException("User not found with id: " + id));
		System.out.println("Deleting user with id : " + id);
		userRep.delete(userData);
		System.out.println("Deletion of id " + id + " was successfull");
		return ResponseEntity.ok().build();
		
	}

}
