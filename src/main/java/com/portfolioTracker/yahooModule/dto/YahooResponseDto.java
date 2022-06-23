package com.portfolioTracker.yahooModule.dto;

import com.portfolioTracker.yahooModule.validation.annotation.Currency;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@Data
public class YahooResponseDto {

    private String ticker;
    @Currency
    private String currency;
    private BigDecimal currentMarketPrice;
    @NotNull
    private List<YahooPriceDto> yahooPriceDtoList;
    @NotNull
    private List<YahooSplitEventDto> yahooSplitEventDtoList;
    private LocalDateTime updateDateTime;

}
