package com.running_platform.service.impl;

import com.running_platform.config.AuthConfig;
import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.SignInRequest;
import com.running_platform.dto.response.TokenResponse;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.TokenType;
import com.running_platform.exception.AppException;
import com.running_platform.repository.AuthRepository;
import com.running_platform.security.AppSecurityUtils;
import com.running_platform.service.AuthenticationService;
import com.running_platform.service.JwtService;
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

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j( topic = "Authenticate")
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    AuthRepository authRepository;
    AuthConfig authConfig;
    @Override
    public TokenResponse getAccessToken(SignInRequest request) {

        Users user = authRepository.findByUsername(request.getUsername()).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        if(!user.isEmailVerified()) {
            throw new AppException(ErrorEnum.EMAIL_NOT_VERIFIED);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(AuthenticationException e) {
            throw new AppException(ErrorEnum.FORBIDDEN);
        }
        Set<GrantedAuthority> roles = new HashSet<>(AppSecurityUtils.convertRolesSetToGrantedAuthorityList(user.getRoles()));
        String accessToken = jwtService.generateToken(user.getId(), roles, TokenType.ACCESSTOKEN);
        String refreshToken = jwtService.generateToken(user.getId(), roles, TokenType.REFRESHTOKEN);

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


            Users user = authRepository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
            Set<GrantedAuthority> roles = new HashSet<>(AppSecurityUtils.convertRolesSetToGrantedAuthorityList(user.getRoles()));
            accessToken = jwtService.generateToken(id, roles, TokenType.ACCESSTOKEN);
        } catch (Exception e) {
            log.error("Error generating refresh token: {}", e.getMessage());
            throw new AppException(ErrorEnum.INTERNAL_SERVER_ERROR);
        }
        return TokenResponse.builder().accessToken(accessToken).refreshToken(null). build();
    }
}
