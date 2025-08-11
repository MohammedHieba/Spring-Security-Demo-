package com.spring.security.demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.spring.security.demo.model.AppUser;
import com.spring.security.demo.repository.UserRepo;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepo userRepo;

	
	public List<AppUser> findAll (){
		
		return userRepo.findAll();
	}
	
    public AppUser findById (Long id){
		
		return userRepo.findById(id).orElse(null);
	}
    
    public AppUser save(AppUser entity) {
		entity.setPassword(entity.getPassword());
		return userRepo.save(entity);
	}

}
