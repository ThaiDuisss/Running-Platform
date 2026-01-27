package com.laundry.config;

import com.laundry.client.UserClient;
import com.laundry.fallback.FallbackBase;
import feign.Target;
import org.springframework.cloud.openfeign.CircuitBreakerNameResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class FeignConfig {
    @Bean
    public FallbackBase<UserClient> userClientFallbackFactory() {
        return new FallbackBase<>(UserClient.class);
    }

    @Bean
    public CircuitBreakerNameResolver circuitBreakerNameResolver() {
        // Ép mọi method trong UserClient đều dùng chung id là "user-service"
        return (String feignClientName, Target<?> target, Method method) -> feignClientName;    }
}
