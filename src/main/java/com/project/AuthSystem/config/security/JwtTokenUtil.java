package com.project.AuthSystem.config.security;

import com.project.AuthSystem.config.exception.custom.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token.expiration.in.second}")
    private long expirationDuration;


    public String generateToken(String email) {
        try {
            Map<String, Object> claims = new HashMap<>();
            Date createdDate = new Date();
            Date expirationDate = new Date(createdDate.getTime() + expirationDuration * 1000);
            SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());


            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(createdDate)
                    .setExpiration(expirationDate)
                    .signWith(key)
                    .compact();
        }catch (Exception ex){
            log.error("generateToken error", ex);
            return null;
        }
    }

    public boolean validateToken(String token){
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT", e);
            throw new AuthenticationException("Invalid Token!!");
        }
    }

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Set the signing key here
                .build() // Build the parser
                .parseClaimsJws(token) // Parse the token
                .getBody(); // Extract the claims
        return claimsResolver.apply(claims);
    }
}
