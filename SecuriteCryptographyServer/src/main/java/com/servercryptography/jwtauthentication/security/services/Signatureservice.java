package com.servercryptography.jwtauthentication.security.services;

import java.security.KeyPair;
import java.security.PublicKey;

import org.springframework.stereotype.Service;

@Service
public interface Signatureservice {
	

	public KeyPair genererCle();

	public byte[] readSignature(String filename);

	public void enregistrerSignature(byte[] sign, String filename);

	public byte[] signer(byte[] hash, KeyPair keyPair);

	boolean verifysign(byte[] hash, byte[] sign, PublicKey pubkey);

}
