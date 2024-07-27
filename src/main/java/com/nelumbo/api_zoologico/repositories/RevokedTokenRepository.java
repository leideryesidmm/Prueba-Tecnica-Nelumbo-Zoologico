package com.nelumbo.api_zoologico.repositories;

import com.nelumbo.api_zoologico.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken,Long> {
    Optional<RevokedToken> findByToken(String token);
    void deleteByExpirationBefore(Date now);
}
