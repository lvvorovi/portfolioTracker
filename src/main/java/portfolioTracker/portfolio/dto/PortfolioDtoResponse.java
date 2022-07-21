package portfolioTracker.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoResponse;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDtoResponse extends RepresentationModel<PortfolioDtoResponse> {

    private String id;
    private String name;
    private String strategy;
    private String currency;
    private String username;
    @JsonInclude(NON_NULL)
    private List<TransactionDtoResponse> transactionList;
    @JsonInclude(NON_NULL)
    private List<DividendDtoResponse> dividendList;

}
