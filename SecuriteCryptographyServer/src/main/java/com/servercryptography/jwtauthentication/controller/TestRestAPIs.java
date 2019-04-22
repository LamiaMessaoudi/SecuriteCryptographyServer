package com.servercryptography.jwtauthentication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.servercryptography.jwtauthentication.repository.RoleRepository;
import com.servercryptography.jwtauthentication.repository.UserRepository;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestRestAPIs {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    
	@GetMapping("/api/test/user")
	@PreAuthorize("hasRole('USER') ")
	public String userAccess() {
		return ">>> User Contents!";
	}
	
	@GetMapping("/api/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}
	
	
}