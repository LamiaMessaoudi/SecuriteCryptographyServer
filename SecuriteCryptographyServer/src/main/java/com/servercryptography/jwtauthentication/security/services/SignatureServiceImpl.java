package com.servercryptography.jwtauthentication.security.services;

import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servercryptography.jwtauthentication.model.Document;
import com.servercryptography.jwtauthentication.repository.DocumentRepository;


@Service
public class SignatureServiceImpl implements Signatureservice{
	
	@Autowired
	DocumentRepository documentRepository;
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
	  public byte[]  signer(byte[] hash ,KeyPair keyPair , String algosign ) 
	  {  byte[] signatureBytes = null;
		  try {
	           Signature signature = Signature.getInstance(algosign);
	           signature.initSign(keyPair.getPrivate(), new SecureRandom());
	           signature.update(hash);
	           signatureBytes = signature.sign();
	     }catch(Exception e) {
	    	 
	     }
		  return signatureBytes;
	  }
	 
	 @Override
	 public boolean verifysign(byte[] hash, byte[] sign , PublicKey pubkey , String algosign) {
		 boolean ok = false ;
		 try {
		 Signature signature = Signature.getInstance(algosign);
		 signature.initVerify(pubkey);
		 signature.update(hash);
		ok= signature.verify(sign);
         System.out.println(ok);
		 }catch(Exception e) {
			 
		 }
		 if(ok)
		 {
		   System.out.println("the document est bien verifier ,ils se sont confirmées ");	 
		 }	 
		 else
		 {
			   System.out.println("ils ne  sont pas  confirmées ");	 

		 }	 
		return ok;
		 
	 }
	  
	 @Override
	  public Document enregistrerSignature(byte[] sign ) {
		 try {File file=new File("sign");
		 FileOutputStream fop=new FileOutputStream(file);
		 fop.write(sign);
		 fop.close();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 Document doc=new Document("signature",sign);
		try {
		 File file=new File("E:\\signature\\sign");
			FileOutputStream fop= new FileOutputStream(file);
			fop.write(sign);
			fop.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return  documentRepository.save(doc);
		
	  }
	  
	  
	 @Override
	  public byte[] readSignature(File file)
	  {   byte[] getBytes = null;
	     try {
	        File files;
	         files = file;
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
