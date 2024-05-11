package com.simpleblogapi.simpleblogapiv2.repositories;

import com.simpleblogapi.simpleblogapiv2.entities.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoutTokenRepository extends JpaRepository<LogoutToken, String> {

}