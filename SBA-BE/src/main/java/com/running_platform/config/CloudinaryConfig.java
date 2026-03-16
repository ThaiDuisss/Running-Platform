package com.running_platform.config;

import com.cloudinary.Cloudinary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryConfig {
AppProperties appProperties;

    @Bean
    public Cloudinary cloudinary() {
        Map<String,String> config = Map.of(
                "cloud_name", appProperties.getCloudinary().getCloudName(),
                "api_key", appProperties.getCloudinary().getApiKey(),
                "api_secret", appProperties.getCloudinary().getApiSecret()
        );

        return new Cloudinary(config);
    }

}
