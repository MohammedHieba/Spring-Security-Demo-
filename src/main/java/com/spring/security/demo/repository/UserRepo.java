package com.spring.security.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.security.demo.model.AppUser;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {
	
	Optional<AppUser> findByUserName (String userName) ;

}
