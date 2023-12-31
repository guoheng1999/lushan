package edu.cuit.lushan.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(value = "lushan")
@Component
@Data
public class LushanConfig {
    private String currentDataRoot;
    private String historyDataRoot;
    private String historyDatasetPicture;
    private String proofRoot;
    private String bufferDataRoot;
    private String linkRoot;
    private String secret;
    private String commentRoot;
}
