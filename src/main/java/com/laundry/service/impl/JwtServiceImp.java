package com.laundry.service.impl;

import com.laundry.enums.ErrorEnum;
import com.laundry.enums.TokenType;
import com.laundry.exception.AppException;
import com.laundry.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j(topic = "JWT_SERVICE")
public class JwtServiceImp implements JwtService {
    @Value("${jwt.accessTokenSecret}")
    String accessTokenSecret;
    @Value("${jwt.refreshTokenSecret}")
    String refreshTokenSecret;
    @Value("${jwt.mailSecret}")
    String mailTokenSecret;
    @Value("${jwt.accessTokenExpiration}")
    long accessTokenExpiration;
    @Value("${jwt.refreshTokenExpiration}")
    long refreshTokenExpiration;
    @Value("${jwt.mailExpiration}")
    long mailExpiration;

    @Override
    public String generateToken(Long id, Collection<? extends GrantedAuthority> roles, TokenType tokenType) {
        long expiration = tokenType == TokenType.ACCESSTOKEN ? accessTokenExpiration : refreshTokenExpiration;
        Key key = getKey(tokenType);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("scope", roles)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String generateEmailToken(String email) {
        long expiration = mailExpiration;
        Key key = getKey(TokenType.MAILTOKEN);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    @Override
    public Long extractToken(String token, TokenType tokenType) {
        if (validateToken(token, tokenType)) {
            Key key = getKey(tokenType);
            String id = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
            return Long.parseLong(id);
        }
        return null;
    }

    public String extractTokenEmail(String token) {
        if (validateToken(token, TokenType.MAILTOKEN)) {
            Key key = getKey(TokenType.MAILTOKEN);
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        }
        log.info("Token invalid");
        throw new AppException(ErrorEnum.FORBIDDEN);
    }


    public boolean validateToken(String token, TokenType tokenType) {
        try {
            Key key = getKey(tokenType);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
           log.error("Time Expire {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid Token {}", e.getMessage());
        }
        return false;
    }

    public Key getKey(TokenType tokenType) {
        switch (tokenType) {
            case ACCESSTOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
            case REFRESHTOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecret));
            case MAILTOKEN:
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(mailTokenSecret));
            default:
                throw new RuntimeException();
        }
    }
}
