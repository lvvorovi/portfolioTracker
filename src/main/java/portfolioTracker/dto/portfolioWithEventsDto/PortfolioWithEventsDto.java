package portfolioTracker.dto.portfolioWithEventsDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoResponse;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class PortfolioWithEventsDto extends RepresentationModel<DividendDtoResponse> {

    private String id;
    private String name;
    private String strategy;
    private String currency;
    private List<TransactionDtoResponse> transactionEntityList;
    private List<DividendDtoResponse> dividendEntityList;
    private String username;

}
