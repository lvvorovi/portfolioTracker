package com.portfolioTracker.yahooModule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "yahoo")
public class YahooModuleConfig {

    private String jsonExtractionStart;
    private String jsonExtractionEnd;


}
