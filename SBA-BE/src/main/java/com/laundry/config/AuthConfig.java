package com.laundry.config;

import com.laundry.service.UserServiceDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "AUTH_CONFIG")
public class AuthConfig {
     UserServiceDetail userServiceDetail;
     CustomizeRequestFilter customizeRequestFilter;

     String[] PUBLIC_ENDPOINTS = {
            "/sign-in", "/register", "/refresh-token", "/introspect",
             "/swagger-ui/**",
             "/v3/api-docs/**",
             "/swagger-ui.html",
             "/webjars/**",
             "/actuator/**",
             "/auth/validateEmail/**",
             "/validateEmail/**",
             "/mail-again/**",
             "error",
             "success",
             "/get-info/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain");
            http.securityMatcher("/**")
                    .authorizeHttpRequests(req -> req.requestMatchers(PUBLIC_ENDPOINTS).permitAll().anyRequest().authenticated())
                    .sessionManagement(mgr -> mgr.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(customizeRequestFilter, UsernamePasswordAuthenticationFilter.class)
                    .csrf(csrf -> csrf.disable());
            return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userServiceDetail.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
