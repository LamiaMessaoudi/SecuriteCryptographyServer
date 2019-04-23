package com.servercryptography.jwtauthentication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servercryptography.jwtauthentication.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
