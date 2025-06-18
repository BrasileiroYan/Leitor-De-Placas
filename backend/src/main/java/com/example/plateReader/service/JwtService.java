package com.example.plateReader.service;

import com.example.plateReader.service.exception.CustomMalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import com.example.plateReader.service.exception.CustomExpiredJwtTokenException;
import com.example.plateReader.service.exception.JwtAuthenticationException;
import com.example.plateReader.Utils.JwtBlacklist;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Autowired
    JwtBlacklist jwtBlacklist;

    public JwtService(JwtBlacklist jwtBlacklist){
        this.jwtBlacklist = jwtBlacklist;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = this.secretKeyString.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
        throw new CustomExpiredJwtTokenException("Token JWT expirado", e);
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException e) {
            throw new CustomMalformedJwtException("Assinatura do token JWT é inválida.", e);
        } catch (MalformedJwtException e) {
            throw new CustomMalformedJwtException("Token JWT malformado.", e);
        } catch (ExpiredJwtException e) {
            throw new CustomExpiredJwtTokenException("Token JWT expirou.", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException("Token JWT não é suportado.", e);
        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException("A string do token JWT está vazia ou é nula.", e);
        }
    }

    public void invalidateToken(String token) {
        Date expirationDate = this.extractExpiration(token);
        jwtBlacklist.invalidateToken(token, expirationDate.toInstant());
    }

}
