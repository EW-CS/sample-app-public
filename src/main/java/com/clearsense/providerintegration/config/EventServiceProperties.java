package com.clearsense.providerintegration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dependencies.user")
public class EventServiceProperties {
    private String username = "PrasadIRevanaki";
    private String password = "VerySecretet";
}
