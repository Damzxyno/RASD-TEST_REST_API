package com.damzxyno.salesportaltest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TimeBasedAccessInterceptor timeBasedAccessInterceptor;
    private final LocationBasedAccessInterceptor locationBasedAccessInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeBasedAccessInterceptor);
        registry.addInterceptor(locationBasedAccessInterceptor);
    }
}
