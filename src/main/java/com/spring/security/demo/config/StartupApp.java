package com.spring.security.demo.config;

import java.util.HashSet;
import java.util.Set;

import com.spring.security.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.spring.security.demo.model.AppUser;
import com.spring.security.demo.model.Role;
import com.spring.security.demo.service.RoleService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartupApp implements CommandLineRunner {
    private final UserService  userService;

    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {


        if (roleService.findAll().isEmpty()) {
            roleService.save(new Role(null, "admin"));
            roleService.save(new Role(null, "user"));
            roleService.save(new Role(null, "employee"));
        }


        if (userService.findAll().isEmpty()) {

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleService.findByName("admin"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleService.findByName("user"));

            Set<Role>  empRoles = new HashSet<>();
            empRoles.add(roleService.findByName("employee"));

            userService.save(new AppUser(null, "Mohamed Hieba", "mohamed", "123", adminRoles, true, true, true, true));
            userService.save(new AppUser(null, "Omar Mohamed", "Omar", "123", userRoles, true, true, true, true));
            userService.save(new AppUser(null, "Yehia Mohamed", "Yehia", "123", empRoles, true, true, true, true));
        }

    }

}
