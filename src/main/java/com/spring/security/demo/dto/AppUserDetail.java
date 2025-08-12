package com.spring.security.demo.dto;

import com.spring.security.demo.model.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class AppUserDetail implements UserDetails {

    private AppUser user;

    private String userName;

    private String password ;

    private List<GrantedAuthority> authorities;

    public AppUserDetail(AppUser user) {
        this.user = user;
        this.userName = user.getUserName();
        this.password = user.getPassword();
        this.authorities = new ArrayList<>();

        if(!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

}
