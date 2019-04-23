package com.servercryptography.jwtauthentication.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servercryptography.jwtauthentication.message.response.JwtResponse;
import com.servercryptography.jwtauthentication.model.Role;
import com.servercryptography.jwtauthentication.model.RoleName;
import com.servercryptography.jwtauthentication.model.User;
import com.servercryptography.jwtauthentication.repository.RoleRepository;
import com.servercryptography.jwtauthentication.repository.UserRepository;
import com.servercryptography.jwtauthentication.security.jwt.JwtProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser( @RequestParam(name="username") String username,@RequestParam(name="password") String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                      username,
                      password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));

    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.POST, headers = {"content-type=multipart/mixed", "content-type=multipart/form-data"},consumes = {"multipart/form-data"})
    public ResponseEntity<User> registerUser(@Valid @RequestPart(required=false,name="image") MultipartFile file,@RequestPart(name="user")String struser) throws JsonParseException, JsonMappingException, IOException {
   	 User user1 = new ObjectMapper().readValue(struser, User.class);

    	if(userRepository.existsByUsername(user1.getUsername())) {
        
              return new ResponseEntity<User>(HttpStatus.NOT_FOUND);	
          }

          if(userRepository.existsByEmail(user1.getEmail())) {
        	  return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
          }

          // Creating user's account
          System.out.println("lamia messssssaoudi");
          System.out.println("hellloddddd "+encoder.encode(user1.getPassword()));

          User user = new User(user1.getName(), user1.getUsername(),
                 user1.getEmail(),encoder.encode(user1.getPassword()));

          Set<Role> roles = new HashSet<>();

      
  	        		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
  	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
  	        		roles.add(userRole);        			
        
          user.setRoles(roles);
          user.setAvatar(file.getBytes());
          userRepository.save(user);
          return new ResponseEntity<User>(user, HttpStatus.OK);


    }
    @RequestMapping(value = "/signupadmin", method = RequestMethod.POST)
    public ResponseEntity<User> registerAdmin(@Valid @RequestBody User user)  {

    	if(userRepository.existsByUsername(user.getUsername())) {
        
              return new ResponseEntity<User>(HttpStatus.NOT_FOUND);	
          }

          if(userRepository.existsByEmail(user.getEmail())) {
        	  return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
          }

          // Creating user's account
          System.out.println("lamia messssssaoudi");
          System.out.println("hellloddddd "+encoder.encode(user.getPassword()));

          User user1 = new User(user.getName(), user.getUsername(),
                 user.getEmail(),encoder.encode(user.getPassword()));

          Set<Role> roles = new HashSet<>();

      
  	        		Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
  	                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
  	        		roles.add(userRole);        			
        
          user1.setRoles(roles);
          userRepository.save(user1);
          return new ResponseEntity<User>(user1, HttpStatus.OK);


    }
    @GetMapping("/findAllUsers")
    public List<User> findAllUsers() {
 		return userRepository.findAll();
 	}
	
	
    
    
    /*****************************************photo user******************************************/
	@GetMapping("/photoUser/{id}")
 public  byte[] photoUser(@PathVariable("id") Long id, HttpServletRequest request) {
  
		Optional<User> a=userRepository.findById(id);
		
		User a1=a.get();
		try {
			if(a1.getAvatar()==null) {
				return null;
				}
			return IOUtils.toByteArray(new ByteArrayInputStream(a1.getAvatar()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 @DeleteMapping("/deleteUser/{id}")
	    public void deleteUser(@PathVariable("id") Long id) {
	 		userRepository.deleteById(id);
	 	}
	 @JsonIgnore
	 @RequestMapping(value = "/updateUser/{id}", method = RequestMethod.PUT, headers = {"content-type=multipart/mixed", "content-type=multipart/form-data"},consumes = {"multipart/form-data"})
     public  void updateUser(@PathVariable("id") Long id,@RequestPart(required=false,name="image") MultipartFile file2,@RequestPart(name="user")String struser) throws IOException
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
		public User getUserByUserName(@PathVariable("username")String username)
		{
			User user=userRepository.findByUsername(username).get();
			return user;
		}
		
 
}