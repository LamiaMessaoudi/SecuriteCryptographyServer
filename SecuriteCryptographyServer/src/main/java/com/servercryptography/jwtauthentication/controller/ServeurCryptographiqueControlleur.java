package com.servercryptography.jwtauthentication.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.servercryptography.jwtauthentication.security.services.CertificatService;
import com.servercryptography.jwtauthentication.security.services.HashageService;
import com.servercryptography.jwtauthentication.security.services.Signatureservice;


@RestController
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"})
@RequestMapping("/api/auth")
public class ServeurCryptographiqueControlleur {
	
	@Autowired 
	 Signatureservice signatureservice;
	
	@Autowired
	HashageService hashageService;
	
	@Autowired
	CertificatService certificatService;
	 @CrossOrigin
	 @RequestMapping(value = "/signer", method = RequestMethod.POST , headers = {"content-type=multipart/mixed", "content-type=multipart/form-data"},consumes = {"multipart/form-data"})
	    public void signer(@RequestParam("file") MultipartFile file ,@RequestPart(name="algosign")String algosign ,@RequestPart(name="algohash")String algohash) {
   
		 try {
			
			 System.out.println(algosign);
			
			byte[] hash=hashageService.hasher(file.getBytes(),algohash);
			hashageService.enregistrerHashage(hash, file.getName());
			KeyPair keyPair=signatureservice.genererCle();
			X509Certificate cert=certificatService.createCertificat(keyPair, file.getName());
			byte[] sign=signatureservice.signer(hash, keyPair ,algosign);
			signatureservice.enregistrerSignature(sign, file.getName());
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    }
	 
	 @CrossOrigin
	 @RequestMapping(value = "/verifysigner", method = RequestMethod.POST , headers = {"content-type=multipart/mixed", "content-type=multipart/form-data"},consumes = {"multipart/form-data"})
	    public void verifysigner(@RequestParam("document") MultipartFile document ,@RequestPart(name="algosign")String algosign ,@RequestPart(name="algohash")String algohash ,@RequestParam("signature") MultipartFile signature ,@RequestParam("certificat") MultipartFile certificat ) {
 
		 try {
			
			
			byte[] hash=hashageService.hasher(document.getBytes(),algohash);
			hashageService.enregistrerHashage(hash,document.getName());
			X509Certificate cert=certificatService.decodeCertificate(this.convert(certificat));
			PublicKey publickey=certificatService.getPublickey(cert);
			byte[] sign=signatureservice.readSignature(this.convert(signature));
			signatureservice.verifysign(hash, sign, publickey , algosign);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    }
	 
	 public static File convert(MultipartFile file)
	 {  
		  File convFile = null;
		  try {  
	       convFile= new File(file.getOriginalFilename());
	     convFile.createNewFile(); 
	     FileOutputStream fos = new FileOutputStream(convFile); 
	     fos.write(file.getBytes());
	     fos.close(); 
	     return convFile;
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
	return convFile;
	 }
	 
}
