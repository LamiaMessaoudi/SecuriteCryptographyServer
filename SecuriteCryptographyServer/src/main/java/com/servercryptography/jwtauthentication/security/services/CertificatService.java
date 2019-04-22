package com.servercryptography.jwtauthentication.security.services;

import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.springframework.stereotype.Service;

@Service
public interface CertificatService {

	PublicKey getPublickey(X509Certificate cert);

	X509Certificate decodeCertificate(String filename);

	X509Certificate createCertificat(java.security.KeyPair keyPair,String filename );

}
