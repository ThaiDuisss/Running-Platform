package com.example.oauth2.config;

import com.example.oauth2.util.AppUtils;
import com.example.oauth2.util.AppWebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;

    @Bean
    public AppUtils appUtils() {
        return new AppUtils(objectMapper);
    }

    @Bean
    public AppWebUtils webUtils() {
        int cookieExpireSeconds = appProperties.getOAuth2().getCookieExpireSeconds();
        return new AppWebUtils(cookieExpireSeconds);
    }
}
