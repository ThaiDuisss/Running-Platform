package com.example.oauth2.security;

import com.example.oauth2.config.AppProperties;
import com.example.oauth2.security.Oauth2.common.SecurityEnums;
import com.example.oauth2.service.webapp.user.UserMapper;
import com.example.oauth2.util.AppUtils;
import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.*;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String HEADER_AUTHORIZATION = HttpHeaders.AUTHORIZATION;
    private static final String BEARER_TOKEN_START = "Bearer ";

    private final Key accessTokenKey;
    private final Key refreshTokenKey;

    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    private final AppProperties appProperties;
    private final UserMapper mapper;
    private final CustomUserDetailsService userDetailsService;


    public JwtTokenProvider(AppProperties appProperties,
                            CustomUserDetailsService userDetailsService,
                            UserMapper mapper) {
        this.appProperties = appProperties;
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;

        this.accessTokenKey =
                Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(
                                appProperties.getJwt().getSecretAccessKey()
                        )
                );

        this.refreshTokenKey =
                Keys.hmacShaKeyFor(
                        Decoders.BASE64.decode(
                                appProperties.getJwt().getSecretRefreshKey()
                        )
                );

        this.accessTokenValidity =
                appProperties.getJwt().getExpirationAccessMillis();

        this.refreshTokenValidity =
                appProperties.getJwt().getExpirationRefreshMillis();
    }

    public String generateAccessToken(Authentication authentication) {
        CustomUserDetails userDetails =(CustomUserDetails) authentication.getPrincipal();
        Set<String> authoritiesSet = AppSecurityUtils.convertGrantedAuthorityListToRolesSet(authentication.getAuthorities());

        Map<String, Object> claimMap = new HashMap<>();
//        claimMap.put("email", userDetails.getEmail());
//        claimMap.put("user", mapper.toEntity(userDetails.getUserEntity()));
        claimMap.put("authorities", authoritiesSet);
//        claimMap.put("attributes", userDetails.getAttributes());

        Date now = new Date();

        Date expiry =
                new Date(now.getTime() + accessTokenValidity);

        return Jwts.builder()
                .setSubject(userDetails.getUserEntity().getId().toString())
                .claim("authorities", authoritiesSet)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(accessTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        Date now = new Date();

        Date expiry =
                new Date(now.getTime() + refreshTokenValidity);

        return Jwts.builder()
                .setSubject(user.getUserEntity().getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(refreshTokenKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthenticationFromToken(SecurityEnums.TokenType tokenType, String token) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenType == SecurityEnums.TokenType.access_token ? accessTokenKey : refreshTokenKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String userId = claims.getSubject();
            try{
                UserDetails userDetails = userDetailsService.loadUserById(Long.parseLong(userId));
                return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

            }catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
    }

    public boolean validateJwtToken(String token, SecurityEnums.TokenType tokenType) {
        try{
            Jwts.parserBuilder().setSigningKey(tokenType == SecurityEnums.TokenType.access_token ? accessTokenKey : refreshTokenKey).build().parseClaimsJws(token);
            return true;
        }catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;

    }

    public String getBearerTokenStart (HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_START)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
