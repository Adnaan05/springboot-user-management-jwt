package com.addy.spring_project.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.addy.spring_project.security.JwtFilter;
import com.addy.spring_project.services.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				auth -> auth
				.requestMatchers(HttpMethod.POST , "/api/users").permitAll()
				.requestMatchers("/api/users/**").authenticated()
				.requestMatchers("/").permitAll()
				.anyRequest().permitAll()
				)
//		.formLogin(form -> form.permitAll().defaultSuccessUrl("/dashboard") )
		.csrf(csrf -> csrf.disable() )
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
		
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		
//		UserDetails user = User.withUsername("Carl")
//				.password(passwordEncoder.encode("user@123") )
//				.roles("USER")
//				.build();
//		
//		UserDetails admin = User.withUsername("Addy")
//				.password(passwordEncoder.encode("admin@123") )
//				.roles("ADMIN")
//				.build();
//		
//		return new InMemoryUserDetailsManager(user, admin);
		return new CustomUserDetailsService();
		
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService() );
		auth.setPasswordEncoder(passwordEncoder() );
		return auth;
		
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(List.of(authenticationProvider() )  );
	}

}
