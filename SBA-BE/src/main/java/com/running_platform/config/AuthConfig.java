package com.running_platform.config;

import com.running_platform.security.CustomAuthenticationEntrypoint;
import com.running_platform.security.CustomUserDetailsService;
import com.running_platform.security.Oauth2.CustomOauth2UserService;
import com.running_platform.security.Oauth2.Oauth2AuthenticationFailureHandler;
import com.running_platform.security.Oauth2.Oauth2AuthenticationSuccessZHandler;
import com.running_platform.security.Oauth2.common.HttpCookieOauth2AuthorizationRequestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "AUTH_CONFIG")
public class AuthConfig {
    CustomUserDetailsService customUserDetailsService;
    CustomizeRequestFilter customizeRequestFilter;
    CustomAuthenticationEntrypoint customAuthenticationEntrypoint;
    CustomOauth2UserService customOauth2UserService;

    Oauth2AuthenticationSuccessZHandler authenticationSuccessZHandler;
    Oauth2AuthenticationFailureHandler authenticationFailureHandler;
    HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository;
    PasswordEncoder passwordEncoder;
    String[] PUBLIC_ENDPOINTS = {
            "/auth/**",
            "/auth/login",
            "/swagger-ui/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/actuator/**",
            "/auth/validateEmail/**",
            "/validateEmail/**",
            "/mail-again/**",
            "error",
            "success",
            "/get-info/**",
            "/oauth2/**",
            "/api/articles"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain");
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(mgr -> mgr.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()   // 🔥 QUAN TRỌNG
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(e -> e.authenticationEntryPoint(customAuthenticationEntrypoint))
                .oauth2Login(o ->
                        o.authorizationEndpoint(a -> a.baseUri("/oauth2/authorize")
                                        .authorizationRequestRepository(httpCookieOauth2AuthorizationRequestRepository)
                                ).redirectionEndpoint(a -> a.baseUri("/oauth2/callback/*"))
                                .userInfoEndpoint(a -> a.userService(customOauth2UserService))
                                .successHandler(authenticationSuccessZHandler)
                                .failureHandler(authenticationFailureHandler))
                .addFilterBefore(customizeRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder);
        authProvider.setUserDetailsService(customUserDetailsService);
        return authProvider;
    }

}
