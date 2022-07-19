package portfolioTracker.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.core.validation.annotation.Currency;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static portfolioTracker.core.ExceptionErrors.*;

@Data
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDtoUpdateRequest {

    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(min = 36, max = 36, message = ID_LENGTH_ERROR_MESSAGE)
    private String id;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = PORTFOLIO_NAME_MAX_LENGTH_ERROR_MESSAGE)
    private String name;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 150, message = PORTFOLIO_NAME_MAX_LENGTH_ERROR_MESSAGE)
    private String strategy;
    @Currency(message = CURRENCY_TYPE_ERROR_MESSAGE)
    private String currency;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = USERNAME_MAX_LENGTH_ERROR_MESSAGE)
    private String username;

}
