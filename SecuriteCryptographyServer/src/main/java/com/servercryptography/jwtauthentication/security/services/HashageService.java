package com.servercryptography.jwtauthentication.security.services;

import org.springframework.stereotype.Service;

@Service
public interface HashageService {
	public byte[] hashe256 (byte[] file);
	public byte[] md5 (byte[] file);
	void enregistrerHashage(byte[] hash, String filename);
	byte[] readhashage(String filename);


}
