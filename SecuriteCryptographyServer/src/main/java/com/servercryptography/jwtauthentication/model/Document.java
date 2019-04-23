package com.servercryptography.jwtauthentication.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Document {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String typedoc;
    @Lob
    private byte[] contenuDoc;
    
	public Document(String typedoc, byte[] contenuDoc) {
		super();
		this.typedoc = typedoc;
		this.contenuDoc = contenuDoc;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTypedoc() {
		return typedoc;
	}
	public void setTypedoc(String typedoc) {
		this.typedoc = typedoc;
	}
	public byte[] getContenuDoc() {
		return contenuDoc;
	}
	public void setContenuDoc(byte[] contenuDoc) {
		this.contenuDoc = contenuDoc;
	}
    
    

}
