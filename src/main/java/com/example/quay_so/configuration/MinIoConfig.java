package com.example.quay_so.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinIoConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String uploadFolder;
}
