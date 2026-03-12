package com.running_platform.config;

import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.TokenType;
import com.running_platform.repository.AuthRepository;
import com.running_platform.service.impl.JwtServiceImp;
import com.running_platform.service.UserServiceDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "CustomizeRequestFilter")
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomizeRequestFilter extends OncePerRequestFilter {
    JwtServiceImp jwtService;
    UserDetailsService userServiceDetail;
    AuthRepository authRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.info("Processing request: {} {}", method, path);

        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if(jwtService.validateToken(token, TokenType.ACCESSTOKEN)) {
                    Long userId = jwtService.extractToken(token, TokenType.ACCESSTOKEN);
                    Users users = authRepository.findById(userId).orElse(null);
                    UserDetails user = userServiceDetail.loadUserByUsername(users.getUsername());
                    if (user != null) {
                        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                user.getUsername(), null, user.getAuthorities()
                        );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        securityContext.setAuthentication(authentication);
                        SecurityContextHolder.setContext(securityContext);                    }
                }
            } catch (Exception e) {
                log.error("Error processing token: {}", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
