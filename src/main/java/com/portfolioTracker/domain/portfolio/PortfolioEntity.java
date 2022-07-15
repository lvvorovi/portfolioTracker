package com.portfolioTracker.domain.portfolio;

import com.portfolioTracker.core.validation.annotation.Currency;
import com.portfolioTracker.domain.dividend.DividendEntity;
import com.portfolioTracker.domain.transaction.TransactionEntity;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Validated
@Entity
@Table(name = "portfolios")
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
