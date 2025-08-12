package com.spring.security.demo.service;

import java.util.List;
import java.util.Optional;

import com.spring.security.demo.dto.AppUserDetail;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.spring.security.demo.model.AppUser;
import com.spring.security.demo.repository.UserRepo;


@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public List<AppUser> findAll (){
		
		return userRepo.findAll();
	}
	
    public AppUser findById (Long id){
		
		return userRepo.findById(id).orElse(null);
	}
    
    public AppUser save(AppUser entity) {
		entity.setPassword(passwordEncoder.encode(entity.getPassword()));
		return userRepo.save(entity);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AppUser> appUser =	userRepo.findByUserName(username);

		if (!appUser.isPresent()) {

			throw new UsernameNotFoundException("This User Not found with selected user name :- " + username);
		}

		return new AppUserDetail(appUser.get());
	}


}
