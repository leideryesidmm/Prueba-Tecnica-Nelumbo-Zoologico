package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.repositories.RevokedTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final RevokedTokenRepository revokedTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpExpiredTokens() {
        Date now = new Date();
        System.out.println(now);
        revokedTokenRepository.deleteByExpirationBefore(now);
    }
}