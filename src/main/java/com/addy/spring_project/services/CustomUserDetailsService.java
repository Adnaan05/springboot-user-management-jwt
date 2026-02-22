package com.addy.spring_project.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.addy.spring_project.entities.UserEntity;
import com.addy.spring_project.repositories.UserRepository;


@Component
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRep;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		UserEntity user = userRep.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found") );
		return new User(user.getName(), user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("USER_ROLE") )  );
		
	}

}
