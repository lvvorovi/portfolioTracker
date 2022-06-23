package com.portfolioTracker.yahooModule.dto;

import com.portfolioTracker.core.contract.SplitEventDto;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Validated
@Data
@EqualsAndHashCode(callSuper = true)
public class YahooSplitEventDto extends SplitEventDto {

//    @AmountOfMoney
//    private BigDecimal amount;
//    @Date
//    private LocalDate date;
//    @NotEmpty
//    private String type;
//    @NotEmpty
//    private String data;
//    @Quantity
//    private BigDecimal numerator;
//    @Quantity
//    private BigDecimal denominator;
//    @NotEmpty
//    private String splitRatio;

    public void setDate(@NotEmpty String seconds) {
        LocalDate yahooDayZeroDate = LocalDate.of(1970, 1, 1);
        super.setDate(yahooDayZeroDate.plusDays(TimeUnit.SECONDS.toDays(Long.parseLong(seconds))));
    }

}
