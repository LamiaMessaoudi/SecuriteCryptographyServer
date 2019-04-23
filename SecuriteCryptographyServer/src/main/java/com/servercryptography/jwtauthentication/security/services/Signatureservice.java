package com.servercryptography.jwtauthentication.security.services;

import java.io.File;
import java.security.KeyPair;
import java.security.PublicKey;

import org.springframework.stereotype.Service;

@Service
public interface Signatureservice {
	

	public KeyPair genererCle();

	public byte[] readSignature(File file);

	public void enregistrerSignature(byte[] sign, String filename);

	public byte[] signer(byte[] hash, KeyPair keyPair , String algosign);

	boolean verifysign(byte[] hash, byte[] sign, PublicKey pubkey , String algosign);

}
