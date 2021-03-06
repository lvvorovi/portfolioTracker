package portfolioTracker.dto.ticker.service;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.ticker.SplitEventDto;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Validated
public interface TickerService {

    Boolean isTickerSupported(@NotBlank String ticker);

    BigDecimal getTickerCurrentPrice(@NotBlank String ticker);

    String getTickerCurrency(@NotBlank String ticker);

    List<SplitEventDto> getSplitEventList(@NotBlank String ticker);

}
