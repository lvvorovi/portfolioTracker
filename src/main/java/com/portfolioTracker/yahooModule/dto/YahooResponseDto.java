package com.portfolioTracker.yahooModule.dto;

import com.portfolioTracker.yahooModule.validation.annotation.AmountOfMoney;
import com.portfolioTracker.yahooModule.validation.annotation.Currency;
import com.portfolioTracker.yahooModule.validation.annotation.Date;
import com.portfolioTracker.yahooModule.validation.annotation.Ticker;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class YahooResponseDto {

    @Ticker
    private String ticker;
    @Currency
    private String currency;
    @AmountOfMoney
    private BigDecimal currentMarketPrice;
    @NotNull
    private List<YahooPriceDto> yahooPriceDtoList;
    @NotNull
    private List<YahooSplitEventDto> yahooSplitEventDtoList;
    @Date
    private LocalDateTime updateDateTime;

}