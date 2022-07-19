package portfolioTracker.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.core.validation.annotation.Currency;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoResponse;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static portfolioTracker.core.ExceptionErrors.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDtoResponse extends RepresentationModel<PortfolioDtoResponse> {

    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(min = 36, max = 36, message = ID_LENGTH_ERROR_MESSAGE)
    private String id;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = PORTFOLIO_NAME_MAX_LENGTH_ERROR_MESSAGE)
    private String name;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 150, message = PORTFOLIO_STRATEGY_MAX_LENGTH_ERROR_MESSAGE)
    private String strategy;
    @Currency(message = CURRENCY_TYPE_ERROR_MESSAGE)
    private String currency;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = USERNAME_MAX_LENGTH_ERROR_MESSAGE)
    private String username;
    @JsonInclude(NON_NULL)
    private List<TransactionDtoResponse> transactionList;
    @JsonInclude(NON_NULL)
    private List<DividendDtoResponse> dividendList;

}
