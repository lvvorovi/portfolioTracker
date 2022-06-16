package com.portfolioTracker.model.portfolio;

import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.model.transaction.TransactionEntity;
import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.ModelName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Validated
@Entity
@Table(name = "portfolios")
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ModelName
    @Column(name = "name")
    private String name;
    @NotEmpty
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
}
