package com.laundry.service.impl;

import com.laundry.config.AuthConfig;
import com.laundry.dto.request.SignInRequest;
import com.laundry.dto.response.TokenResponse;
import com.laundry.entity.Users;
import com.laundry.enums.ErrorEnum;
import com.laundry.enums.TokenType;
import com.laundry.exception.AppException;
import com.laundry.repository.AuthRepository;
import com.laundry.service.AuthenticationService;
import com.laundry.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j( topic = "Authenticate")
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    AuthRepository authRepository;
    AuthConfig authConfig;
    RedisService redisService;
    @Override
    public TokenResponse getAccessToken(SignInRequest request) {

        Users user = authRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(AuthenticationException e) {
            throw new AppException(ErrorEnum.FORBIDDEN);
        }
        Set<GrantedAuthority> roles = new HashSet<>();
        user.getRoles().stream().map(role ->roles.add(role));
        String accessToken = jwtService.generateToken(user.getId(), roles, TokenType.ACCESSTOKEN);
        String refreshToken = jwtService.generateToken(user.getId(), roles, TokenType.REFRESHTOKEN);
        String key = redisService.normalKey("refresh" + user.getId());
        redisService.setTimeToLive(key, refreshToken, 7, TimeUnit.DAYS);
        return TokenResponse.builder().refreshToken(refreshToken).accessToken(accessToken).build();
    }
    @Override
    public TokenResponse getRefreshToken(String request) {
        if (!StringUtils.hasLength(request)) {
            throw new AppException(ErrorEnum.REFRESH_TOKEN_NOT_FOUND);
        }
        String accessToken;
        try {
            Long id = jwtService.extractToken(request, TokenType.REFRESHTOKEN);
            String key = redisService.normalKey("refresh" + id);
            String storedRefreshToken = (String) redisService.get(key);

            if (storedRefreshToken == null || !storedRefreshToken.equals(request)) {
                throw new AppException(ErrorEnum.REFRESH_TOKEN_NOT_FOUND);
            }
            Users user = authRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
            Set<GrantedAuthority> roles = new HashSet<>();
            user.getRoles().stream().map(roles1 -> roles.add(roles1));
            accessToken = jwtService.generateToken(id, roles, TokenType.ACCESSTOKEN);
        } catch (Exception e) {
            log.error("Error generating refresh token: {}", e.getMessage());
            throw new AppException(ErrorEnum.INTERNAL_SERVER_ERROR);
        }
        return TokenResponse.builder().accessToken(accessToken).refreshToken(null). build();
    }
}
