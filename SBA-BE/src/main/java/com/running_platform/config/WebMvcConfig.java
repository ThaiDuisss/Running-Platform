package com.running_platform.config;

import com.running_platform.util.StringToEnumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final AppProperties appProperties;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverter());
        WebMvcConfigurer.super.addFormatters(registry);

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
        pageableHandlerMethodArgumentResolver.setFallbackPageable(PageRequest.of(appProperties.getDefaults().getDefaultPageStart(), appProperties.getDefaults().getDefaultPageSize()));
        resolvers.add(pageableHandlerMethodArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}