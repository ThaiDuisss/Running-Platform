package com.running_platform.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
//    @Value("${myapp.cloudinary.cloud-name}")
//    private String cloudName;
//    @Value("${myapp.cloudinary.api-key}")
//    private String apiKey;
//    @Value("${myapp.cloudinary.api-secret}")
//    private String apiSecret;
    @Autowired
    AppProperties properties;

    @Bean
    public Cloudinary cloudinary(AppProperties appProperties) {
        Map<String,String> config = Map.of(
                "cloud_name", appProperties.getCloudinary().getCloudName(),
                "api_key", appProperties.getCloudinary().getApiKey(),
                "api_secret", appProperties.getCloudinary().getApiSecret()
        );

        return new Cloudinary(config);
    }

}
