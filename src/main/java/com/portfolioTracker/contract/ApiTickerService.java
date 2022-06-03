package com.portfolioTracker.contract;

import com.portfolioTracker.externalApi.yahoo.dto.YahooEvent;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public interface ApiTickerService {

    Boolean isTickerSupported(@NotNull String ticker);

    BigDecimal getTickerCurrentPrice(@NotNull String ticker);

    String getTickerCurrency(@NotNull String ticker);

    List<YahooEvent> getSplitEventList(@NotNull String ticker);

}
