package com.addy.spring_project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.addy.spring_project.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByUsername(String username);

}
