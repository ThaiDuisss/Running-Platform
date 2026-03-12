package com.example.oauth2.security;

import com.example.oauth2.security.Oauth2.common.SecurityEnums;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider provider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = provider.getBearerTokenStart(request);
            if (StringUtils.hasText(jwt) && this.provider.validateJwtToken(jwt, SecurityEnums.TokenType.access_token)) {
                Authentication authentication = this.provider.getAuthenticationFromToken( SecurityEnums.TokenType.access_token, jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            log.info("Security exception Expired JWT token for user {} - {}", ex.getClaims().getSubject(), ex.getMessage());
            response.sendError(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(), "Expired JWT token");
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.info("Security exception {} ", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
