package com.portfolioTracker.yahooModule.dto;

import com.portfolioTracker.yahooModule.validation.annotation.AmountOfMoney;
import com.portfolioTracker.yahooModule.validation.annotation.Date;
import com.portfolioTracker.yahooModule.validation.annotation.Quantity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Validated
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class YahooSplitEventDto {

    @AmountOfMoney
    private BigDecimal amount;
    @Date
    private LocalDate date;
    @NotEmpty
    private String type;
    @NotEmpty
    private String data;
    @Quantity
    private BigDecimal numerator;
    @Quantity
    private BigDecimal denominator;
    @NotEmpty
    private String splitRatio;

    public void setDate(@NotEmpty String seconds) {
        LocalDate yahooDayZeroDate = LocalDate.of(1970, 1, 1);
        this.date = yahooDayZeroDate.plusDays(TimeUnit.SECONDS.toDays(Long.parseLong(seconds)));
    }

}
