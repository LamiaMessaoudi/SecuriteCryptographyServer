package com.servercryptography.jwtauthentication.security.services;

import java.io.File;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;

import com.servercryptography.jwtauthentication.model.Document;

@Service
public interface CertificatService {

	PublicKey getPublickey(X509Certificate cert);

	X509Certificate decodeCertificate(File file);

	Document createCertificat(java.security.KeyPair keyPair,String filename );

}
