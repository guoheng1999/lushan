package edu.cuit.lushan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(value = "lushan")
@Component
@Data
public class LushanConfig {
    private String dataRoot;
    private String proofRoot;
}
