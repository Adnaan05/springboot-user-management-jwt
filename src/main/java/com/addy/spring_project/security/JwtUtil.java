package com.addy.spring_project.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtil {
	
	private static final String SECRET_KEY_STRING = "DyRMgSS51a4lSJ7mU3OTbyeT8JzpwU4H";
	private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes() );
	
	
	public String tokenGenerator(UserDetails userDetails) {
		return Jwts.builder()
				.subject(userDetails.getUsername() )
				.issuedAt(new Date() )
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60) )
				.signWith(SECRET_KEY, Jwts.SIG.HS256)
				.compact();
		
		
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername() );
		
	}
	
	public String extractUsername(String token) {
		return Jwts.parser()
				.verifyWith(SECRET_KEY)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

}
