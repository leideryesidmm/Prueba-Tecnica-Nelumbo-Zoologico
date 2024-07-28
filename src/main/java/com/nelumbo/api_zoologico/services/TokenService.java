package com.nelumbo.api_zoologico.services;

import com.nelumbo.api_zoologico.repositories.RevokedTokenRepository;
import com.nelumbo.api_zoologico.security.jwt.JwtService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtService jwtService;


    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanUpExpiredTokens() {
        Date now = new Date();
        System.out.println(now);
        revokedTokenRepository.deleteByExpirationBefore(now);
    }
    public String getToken() {
        return (Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(request -> request.getHeader("Authorization"))
                .orElse(null)).substring(7);
    }
    public Long getUserId() {
        return jwtService.getUserId(getToken());
    }

}