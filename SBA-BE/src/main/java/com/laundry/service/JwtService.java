package com.laundry.service;

import com.laundry.enums.TokenType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {
 String generateToken(Long id, Collection<? extends GrantedAuthority> roles, TokenType tokenType);
 Long extractToken(String token, TokenType tokenType);
}


