package com.servercryptography.jwtauthentication.controller;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.servercryptography.jwtauthentication.security.services.CertificatService;
import com.servercryptography.jwtauthentication.security.services.HashageService;
import com.servercryptography.jwtauthentication.security.services.Signatureservice;


@RestController
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4300"})
public class ServeurCryptographiqueControlleur {
	
	@Autowired 
	 Signatureservice signatureservice;
	
	@Autowired
	HashageService hashageService;
	
	@Autowired
	CertificatService certificatService;
	
	 @RequestMapping(value = "/signer", method = RequestMethod.POST)
	    public void signer(@RequestParam("file") MultipartFile file) {
   
		 try {
			
			
			byte[] hash=hashageService.hashe256(file.getBytes());
			hashageService.enregistrerHashage(hash, file.getName());
			KeyPair keyPair=signatureservice.genererCle();
			X509Certificate cert=certificatService.createCertificat(keyPair, file.getName());
			byte[] sign=signatureservice.signer(hash, keyPair);
			signatureservice.enregistrerSignature(sign, file.getName());
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    }
	 
	 
	 @RequestMapping(value = "/verifysigner", method = RequestMethod.POST)
	    public void verifysigner(@RequestParam("file") MultipartFile file) {

		 try {
			
			
			byte[] hash=hashageService.hashe256(file.getBytes());
			hashageService.enregistrerHashage(hash, file.getName());
			X509Certificate cert=certificatService.decodeCertificate(file.getName());
			PublicKey publickey=certificatService.getPublickey(cert);
			byte[] sign=signatureservice.readSignature(file.getName());
			signatureservice.verifysign(hash, sign, publickey);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    }
	 
}
