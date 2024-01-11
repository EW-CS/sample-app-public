package com.clearsense.providerintegration.adapters.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "cors.allowed")
public class CORSOriginProperties {

    private List<String> origins;

}
