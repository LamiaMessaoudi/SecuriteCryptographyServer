package com.servercryptography.jwtauthentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.servercryptography.jwtauthentication.repository.RoleRepository;
import com.servercryptography.jwtauthentication.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RestApiUser {
	  @Autowired
	    UserRepository userRepository;
	    @Autowired
	    RoleRepository roleRepository;

}
