package com.nelumbo.apizoologico.security.jwt;

import com.nelumbo.apizoologico.entities.RevokedToken;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import com.nelumbo.apizoologico.repositories.RevokedTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtService {
    @Value("${SECRET_KEY}")
    private String secretKey;
    private final RevokedTokenRepository revokedTokenRepository;

    public JwtService(RevokedTokenRepository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    public String getToken(Map<String,Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes=Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !isTokenRevoked(token));
    }

    private Claims getAllClaims(String token)
    {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims=getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token)
    {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token)
    {
        return getExpiration(token).before(new Date());
    }
    private boolean isTokenRevoked(String token) {
        return revokedTokenRepository.findByToken(token).isPresent();
    }

    public void revokeToken(String token) {
        Date expiration = new Date();

        RevokedToken revokedToken = RevokedToken.builder()
                .token(token)
                .expiration(expiration)
                .build();
        revokedTokenRepository.save(revokedToken);
    }
    public Long getUserId(String token) {
        Integer idInteger = getClaim(token, claims -> claims.get("id", Integer.class));
        return idInteger != null ? idInteger.longValue() : null;
    }

}
