package com.servercryptography.jwtauthentication.security.services;

import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;


@Service
public class SignatureServiceImpl implements Signatureservice{
	
	
	 @Override 
	 public KeyPair genererCle() {
		 KeyPair keyPair = null;
		 Security.addProvider(new BouncyCastleProvider());

			try {
		    KeyPairGenerator  keyPaiGen= KeyPairGenerator.getInstance("RSA", "BC");
		    keyPaiGen.initialize(1024, new SecureRandom());
		    keyPair = keyPaiGen.generateKeyPair();
			}catch(Exception e) {
				e.printStackTrace();
			}
			return keyPair;
	 }
	  
	 @Override
	  public byte[]  signer(byte[] hash ,KeyPair keyPair ) 
	  {  byte[] signatureBytes = null;
		  try {
	           Signature signature = Signature.getInstance("SHA1withRSA");
	           signature.initSign(keyPair.getPrivate(), new SecureRandom());
	           signature.update(hash);
	           signatureBytes = signature.sign();
	     }catch(Exception e) {
	    	 
	     }
		  return signatureBytes;
	  }
	 
	 @Override
	 public boolean verifysign(byte[] hash, byte[] sign , PublicKey pubkey) {
		 boolean ok = false ;
		 try {
		 Signature signature = Signature.getInstance("SHA1withRSA");
		 signature.initVerify(pubkey);
		 signature.update(hash);
		ok= signature.verify(sign);
         System.out.println(ok);
		 }catch(Exception e) {
			 
		 }
		return ok;
		 
	 }
	  
	 @Override
	  public void enregistrerSignature(byte[] sign , String filename) {
		  try {
		    FileOutputStream fop = null;
			File file;
			file = new File("E:\\signature\\"+filename);
			fop = new FileOutputStream(file);
	        fop.write(sign);
		    fop.close();
		  }catch(Exception e) {
			  
		  } 
	  }
	  
	  
	 @Override
	  public byte[] readSignature(String filename)
	  {   byte[] getBytes = null;
	     try {
	        File file;
	         file = new File("E:\\signature\\"+filename);
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
