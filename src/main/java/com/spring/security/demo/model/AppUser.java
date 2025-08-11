package com.spring.security.demo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity()
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	
	private String fullName;
	
	private String userName;
	
	private String password ;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles" ,
	    joinColumns = @JoinColumn(name = "user_id"),
	    inverseJoinColumns = @JoinColumn(name = "role_id"))
	@OrderColumn(name = "id")
	private Set<Role> roles = new HashSet<>();
	
	private boolean isEnabled;
	
	private boolean isCredentialsNonExpired;
	
	private boolean isAccountNonLocked;
	
	private boolean isAccountNonExpired;
	
	
	public AppUser(Long id) {
		super();
		this.id = id;
	}

}
