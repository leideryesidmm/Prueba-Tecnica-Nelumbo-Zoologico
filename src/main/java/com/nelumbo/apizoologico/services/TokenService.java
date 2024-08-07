package com.nelumbo.apizoologico.services;

import com.nelumbo.apizoologico.exceptions.ResourceNotFoundException;
import com.nelumbo.apizoologico.repositories.RevokedTokenRepository;
import com.nelumbo.apizoologico.security.jwt.JwtService;
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
        revokedTokenRepository.deleteByExpirationBefore(now);
    }
    public String getToken() {
        return (Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(request -> request.getHeader("Authorization"))
                .orElseThrow(() -> new ResourceNotFoundException("Requiere autenticación por token"))).substring(7);
    }
    public Long getUserId() {
        return jwtService.getUserId(getToken());
    }

}