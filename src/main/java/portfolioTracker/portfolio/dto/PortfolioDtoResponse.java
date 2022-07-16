package portfolioTracker.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.core.validation.annotation.Currency;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDtoResponse extends RepresentationModel<PortfolioDtoResponse> {

    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String strategy;
    @Currency
    private String currency;
    @NotBlank
    private String username;
    @JsonInclude(NON_NULL)
    private List<TransactionDtoResponse> transactionList;
    @JsonInclude(NON_NULL)
    private List<DividendDtoResponse> dividendList;

}
