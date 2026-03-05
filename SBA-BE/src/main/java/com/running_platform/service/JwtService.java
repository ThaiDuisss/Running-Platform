package com.running_platform.service;

import com.running_platform.enums.TokenType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtService {
 String generateToken(Long id, Collection<? extends GrantedAuthority> roles, TokenType tokenType);
 Long extractToken(String token, TokenType tokenType);
}


