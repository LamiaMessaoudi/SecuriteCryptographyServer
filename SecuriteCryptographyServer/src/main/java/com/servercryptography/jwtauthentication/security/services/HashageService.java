package com.servercryptography.jwtauthentication.security.services;

import org.springframework.stereotype.Service;

@Service
public interface HashageService {
	public byte[] hasher (byte[] file , String algo);
	void enregistrerHashage(byte[] hash, String filename);
	byte[] readhashage(String filename);


}
