package portfolioTracker.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import portfolioTracker.core.validation.annotation.Currency;
import portfolioTracker.dividend.domain.DividendEntity;
import portfolioTracker.transaction.domain.TransactionEntity;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Validated
@Entity
@Table(name = "portfolios")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioEntity {

    @Id
    private String id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotBlank
    @Column(name = "strategy")
    private String strategy;
    @Currency
    @Column(name = "currency")
    private String currency;
    @Column(name = "transaction_event")
    @OneToMany(mappedBy = "portfolio")
    private List<TransactionEntity> transactionEntityList;
    @Column(name = "dividend_event")
    @OneToMany(mappedBy = "portfolio")
    private List<DividendEntity> dividendEntityList;
    @Column(name = "username")
    @NotBlank
    private String username;
}
