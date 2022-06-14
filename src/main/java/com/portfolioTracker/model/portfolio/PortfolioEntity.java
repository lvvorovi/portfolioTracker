package com.portfolioTracker.model.portfolio;

import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.ModelName;
import com.portfolioTracker.model.transaction.TransactionEntity;
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
    @Column(name = "trade_transaction")
    @OneToMany(mappedBy = "portfolio")
    private List<TransactionEntity> transactionEntities;
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<TransactionEntity> getTransactionEntities() {
        return transactionEntities;
    }

    public void setTransactionEntities(List<TransactionEntity> transactionEntities) {
        this.transactionEntities = transactionEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioEntity entity = (PortfolioEntity) o;
        return Objects.equals(id, entity.id) && Objects.equals(name, entity.name) && Objects.equals(strategy, entity.strategy) && Objects.equals(currency, entity.currency) && Objects.equals(transactionEntities, entity.transactionEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, strategy, currency, transactionEntities);
    }

    @Override
    public String toString() {
        return "PortfolioEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", strategy='" + strategy + '\'' +
                ", currency='" + currency + '\'' +
                ", transactionEntities=" + transactionEntities +
                '}';
    }*/
}
