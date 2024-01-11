package com.clearsense.providerintegration.adapters.cors;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CORSOriginProperties.class)
public class CORSOriginConfiguration {

    private final CORSOriginProperties properties;

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(properties.getOrigins());
        corsConfig.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT"));
        corsConfig.setAllowedHeaders(List.of("Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Access-Control-Allow-Origin", "Authorization", "refreshToken"));
        corsConfig.setAllowCredentials(false);
        return corsConfig;
    }
}
