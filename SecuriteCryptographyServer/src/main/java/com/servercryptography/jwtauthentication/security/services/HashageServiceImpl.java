package com.servercryptography.jwtauthentication.security.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

@Service
public class HashageServiceImpl implements HashageService {

	@Override
	public byte[] hashe256(byte[] file) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return digest.digest(file);
		
	}

	
	
	@Override
	public byte[] md5(byte[] file) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return digest.digest(file);
		
	}
	
	@Override
	public void enregistrerHashage(byte[] hash , String filename) {
		  try {
		    FileOutputStream fop = null;
			File file;
			file = new File("E:\\hashage\\"+filename);
			fop = new FileOutputStream(file);
	        fop.write(hash);
		    fop.close();
		  }catch(Exception e) {
			  
		  } 
	  }
	  
	  @Override
	  public byte[] readhashage(String filename)
	  {
		  byte[] getBytes = null;
		     try {
		        File file;
		         file = new File("E:\\hashage\\"+filename);
		        getBytes = new byte[(int) file.length()];
		        InputStream is = new FileInputStream(file);
		        is.read(getBytes);
		        is.close();
		    
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return getBytes;
	}


}