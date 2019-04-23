package com.servercryptography.jwtauthentication.security.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servercryptography.jwtauthentication.model.Document;
import com.servercryptography.jwtauthentication.repository.DocumentRepository;

@Service
public class CertificatServiceImpl implements CertificatService {
	@Autowired
	DocumentRepository documentRepository;
	@Override
	public PublicKey getPublickey(X509Certificate cert ) {
		return cert.getPublicKey();
	}
	
	@Override
	public  X509Certificate decodeCertificate(File file) {
	    try (
	    		PemReader preader = new PemReader(new FileReader(file));)
	    {
	        byte[] requestBytes = preader.readPemObject().getContent();
	        CertificateFactory factory = CertificateFactory.getInstance("X.509");
	        ByteArrayInputStream in = new ByteArrayInputStream(requestBytes);
	        X509Certificate result = (X509Certificate) factory.generateCertificate(in);
	        System.out.println(result.getPublicKey());
	        return result;
	    } catch (IOException | CertificateException e) {
	        RuntimeException exception = new RuntimeException("An unexpected exception occurred while attempting to decode a certificate.", e);
	        throw exception;
	    }
	}
	    
	  @Override  
      public  Document createCertificat(java.security.KeyPair keyPair ,String filename) {
		  Document doc=null;
    	  Security.addProvider(new BouncyCastleProvider());

    	  Date validityBeginDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
          // in 2 years
          Date validityEndDate = new Date(System.currentTimeMillis() + 2 * 365 * 24 * 60 * 60 * 1000);

	

          // GENERATE THE X509 CERTIFICATE
          X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
          X500Principal dnName = new X500Principal("CN=Mohamed Ajel");

          certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
          certGen.setSubjectDN(dnName);
          certGen.setIssuerDN(dnName); // use the same
          certGen.setNotBefore(validityBeginDate);
          certGen.setNotAfter(validityEndDate);
          certGen.setPublicKey(keyPair.getPublic());
          certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

          X509Certificate cert = null;
		try {
			cert = certGen.generate(keyPair.getPrivate(), "BC");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
		
          PemWriter pemWriter = null;
		try {
			doc=new Document("certificat",cert.getEncoded());
			pemWriter = new PemWriter(new FileWriter("E:\\certificat\\"+filename+".pem"));
		
				pemWriter.writeObject(new PemObject("CERTIFICATE", cert.getEncoded()));
				pemWriter.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		    return documentRepository.save(doc);

    	  }  
      
	  
}
