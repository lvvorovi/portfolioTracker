package com.portfolioTracker.yahooModule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Component
@ConfigurationProperties(prefix = "yahoo")
public class YahooModuleConfig {

    private String jsonExtractionStart;
    private String jsonExtractionEnd;
    private BigDecimal baseRateClientBuys;
    private BigDecimal baseRateClientSells;
    private Long validityOfResponseMinutes;
    private String coreUri;
    private String period1Uri;
    private String period2Uri;
    private String finalUri;
    private Integer responseHistoryStartYear;
    private LocalDate dataReferenceDate;

    public void setDataReferenceDate(String date) {
        this.dataReferenceDate = LocalDate.parse(date);
    }
}
