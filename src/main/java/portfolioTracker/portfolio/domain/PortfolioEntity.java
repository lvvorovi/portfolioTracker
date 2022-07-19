package portfolioTracker.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.core.validation.annotation.Currency;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.transaction.domain.TransactionEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static portfolioTracker.core.ExceptionErrors.*;

@Data
@Validated
@Entity
@Table(name = "portfolios")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioEntity {

    @Id
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 36, min = 36, message = ID_LENGTH_ERROR_MESSAGE)
    @Column(name = "id")
    private String id;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = PORTFOLIO_NAME_MAX_LENGTH_ERROR_MESSAGE)
    @Column(name = "name")
    private String name;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = PORTFOLIO_STRATEGY_MAX_LENGTH_ERROR_MESSAGE)
    @Column(name = "strategy")
    private String strategy;
    @Currency(message = CURRENCY_TYPE_ERROR_MESSAGE)
    @Column(name = "currency")
    private String currency;
    @Column(name = "transaction_event")
    @OneToMany(mappedBy = "portfolio")
    private List<TransactionEntity> transactionEntityList;
    @Column(name = "dividend_event")
    @OneToMany(mappedBy = "portfolio")
    private List<DividendEntity> dividendEntityList;
    @NotBlank(message = NOT_BLANK_ERROR_MESSAGE)
    @Size(max = 50, message = USERNAME_MAX_LENGTH_ERROR_MESSAGE)
    @Column(name = "username")
    private String username;
}
