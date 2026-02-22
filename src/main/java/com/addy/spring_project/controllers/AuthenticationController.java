package com.addy.spring_project.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.addy.spring_project.entities.UserEntity;
import com.addy.spring_project.security.JwtUtil;



@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@PostMapping("/login")
	public ResponseEntity<Map<String, String> > login(@RequestBody UserEntity user){
		try {
			Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword() ) );
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			
			String token = jwtUtil.tokenGenerator(userDetails);
			
			return ResponseEntity.ok(Map.of("token", token));
			
		}
		catch (Exception e){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("Error", "Invalid Username or Password") );
		}
		
	}
	
	

}
