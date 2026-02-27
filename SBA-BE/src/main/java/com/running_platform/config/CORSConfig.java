package com.running_platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CORSConfig implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE");
    }
}
