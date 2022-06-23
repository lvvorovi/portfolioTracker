package com.portfolioTracker.yahooModule.dto;

import com.portfolioTracker.core.contract.SplitEventDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Validated
@Data
@EqualsAndHashCode(callSuper = true)
public class YahooSplitEventDto extends SplitEventDto {

    public void setDate(@NotBlank String seconds) {
        LocalDate yahooDayZeroDate = LocalDate.of(1970, 1, 1);
        super.setDate(yahooDayZeroDate.plusDays(TimeUnit.SECONDS.toDays(Long.parseLong(seconds))));
    }

}
