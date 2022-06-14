package com.portfolioTracker.contract;

import com.portfolioTracker.validation.annotation.Ticker;
import com.portfolioTracker.externalApi.yahoo.dto.YahooEvent;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Validated
public interface ApiTickerService {

    Boolean isTickerSupported(@Ticker String ticker);

    BigDecimal getTickerCurrentPrice(@Ticker String ticker);

    String getTickerCurrency(@Ticker String ticker);

    List<YahooEvent> getSplitEventList(@Ticker String ticker);

}
