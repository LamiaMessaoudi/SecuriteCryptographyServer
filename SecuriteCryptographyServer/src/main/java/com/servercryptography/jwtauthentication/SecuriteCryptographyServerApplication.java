package com.servercryptography.jwtauthentication;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@ComponentScan({ "com.servercryptography.jwtauthentication.controller","com.servercryptography.jwtauthentication.message.response" ,"com.servercryptography.jwtauthentication.model","com.servercryptography.jwtauthentication.repository","com.servercryptography.jwtauthentication.security","com.servercryptography.jwtauthentication.security.jwt","com.servercryptography.jwtauthentication.security.services"})

@SpringBootApplication(scanBasePackages= {"com.servercryptography.jwtauthentication.controller","com.servercryptography.jwtauthentication.message.response","com.servercryptography.jwtauthentication.model","com.servercryptography.jwtauthentication.repository","com.servercryptography.jwtauthentication.security","com.servercryptography.jwtauthentication.security.jwt","com.servercryptography.jwtauthentication.security.services"})
public class SecuriteCryptographyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuriteCryptographyServerApplication.class, args);
	}
}
