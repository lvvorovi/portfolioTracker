package com.portfolioTracker.contract;

import com.portfolioTracker.yahooModule.dto.YahooSplitEventDto;
import com.portfolioTracker.core.validation.annotation.Ticker;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

@Validated
public interface ApiTickerService {

    Boolean isTickerSupported(@Ticker String ticker);

    BigDecimal getTickerCurrentPrice(@Ticker String ticker);

    String getTickerCurrency(@Ticker String ticker);

    List<YahooSplitEventDto> getSplitEventList(@Ticker String ticker); //TODO contract for EVENT, that will be extended by YahooSplitEventDto

}
