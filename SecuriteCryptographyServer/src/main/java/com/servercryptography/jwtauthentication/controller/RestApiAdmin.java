package com.servercryptography.jwtauthentication.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servercryptography.jwtauthentication.model.Role;
import com.servercryptography.jwtauthentication.model.RoleName;
import com.servercryptography.jwtauthentication.model.User;
import com.servercryptography.jwtauthentication.repository.RoleRepository;
import com.servercryptography.jwtauthentication.repository.UserRepository;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class RestApiAdmin {
	
	  @Autowired
	    UserRepository userRepository;
	    @Autowired
	    RoleRepository roleRepository;
	
	@GetMapping("/findAllUsers")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> findAllUsers() {
		Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_USER);
        Role role=userRole.get();
        List<Role>r=new ArrayList<Role>();
        r.add(role);
		return userRepository.findByRoles( r);
	}

	   @GetMapping("/findAllUser")
	   @PreAuthorize("hasRole('ADMIN')")
	    public List<User> findAllUser() {
	 		return userRepository.findAll();
	 	}
	   
	   
	   
	   
	   
	   @DeleteMapping("/deleteUser/{id}")
	   @PreAuthorize("hasRole('ADMIN')")
	    public void deleteUser(@PathVariable("id") Long id) {
	 		userRepository.deleteById(id);
	 	}
	 @JsonIgnore
	 @RequestMapping(value = "/updateUser/{id}", method = RequestMethod.PUT, headers = { "content-type=multipart/mixed", "content-type=multipart/form-data"},consumes = {"multipart/form-data"})
	 @PreAuthorize("hasRole('ADMIN')")
	 public  void updatePostt(@PathVariable("id") Long id,@RequestPart(required=false,name="image") MultipartFile file2,@RequestPart(name="user")String struser) throws IOException
	 {       
		    User user = new ObjectMapper().readValue(struser, User.class);
			Optional<User> userOptional=userRepository.findById(id);
			User u1=userOptional.get();
			if(user.getUsername()!=null)
				u1.setUsername(user.getUsername());
			if(user.getName()!=null)
				u1.setName(user.getName());;
			if(user.getEmail()!=null)
				u1.setEmail(user.getEmail());;
			
			if(file2 !=null)
				u1.setAvatar(file2.getBytes());
		    userRepository.save(u1);

	 }
		@JsonIgnore
		@PutMapping(value="/changeRole/{id}")
		@PreAuthorize("hasRole('ADMIN')")
		 public  void changeRoleUser(@PathVariable("id") Long id)
		 {
			Optional<User> user=userRepository.findById(id);
			
			Role userRole2 = roleRepository.findByName(RoleName.ROLE_ADMIN).get();
			Role userRole1 = roleRepository.findByName(RoleName.ROLE_USER).get();
			User user1=user.get();
			Set<Role> rolesuser = user1.getRoles();
			Iterator anIterator=rolesuser.iterator();
			Role r=(Role) anIterator.next();
	          Set<Role> roles = new HashSet<>();
	          if(r.getId()==1)
	          {
	        	  roles.add(userRole2);
	        	  user1.setRoles(roles);
	          }
	          if(r.getId()==2)
	          {
	        	  roles.add(userRole1);
	        	  user1.setRoles(roles);
	          }
	          userRepository.save(user1);

			
		 }
		
		
		@GetMapping(value="/getUser/{username}")
		@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
		public User getUserByUserName(@PathVariable("username")String username)
		{
			User user=userRepository.findByUsername(username).get();
			return user;
		}
		
}
