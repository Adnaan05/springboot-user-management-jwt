package com.addy.spring_project.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.addy.spring_project.services.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String authHeader = request.getHeader("Authentication");
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			// JWT Validation Failed. Hence, send control to the next filter in the FilterChain.
			filterChain.doFilter(request, response);
			return;
			
		}
		
		String token = authHeader.substring(7);
		
		try {
			String username = jwtUtil.extractUsername(token);
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if(jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = 
							new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), 
									userDetails.getAuthorities() );
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request) );
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
			
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
			
		}
		
		filterChain.doFilter(request, response);
		
		
		
	}

}
