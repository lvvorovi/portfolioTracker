package com.portfolioTracker.model.portfolio.dto;

import com.portfolioTracker.validation.annotation.Currency;
import com.portfolioTracker.validation.annotation.ModelName;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Validated
public class PortfolioResponseDto extends RepresentationModel<PortfolioResponseDto> {

    private Long id;
    @ModelName
    private String name;
    @NotEmpty
    private String strategy;
    @Currency
    private String currency;
    @NotNull
    private List<TransactionResponseDto> transactions;
    @NotNull
    private List<DividendResponseDto> dividends;
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

    public List<TransactionResponseDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionResponseDto> transactions) {
        this.transactions = transactions;
    }

    public List<DividendResponseDto> getDividends() {
        return dividends;
    }

    public void setDividends(List<DividendResponseDto> dividends) {
        this.dividends = dividends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortfolioResponseDto that = (PortfolioResponseDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(strategy, that.strategy)
                && Objects.equals(currency, that.currency)
                && Objects.equals(transactions, that.transactions)
                && Objects.equals(dividends, that.dividends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, strategy, currency, transactions, dividends);
    }

    @Override
    public String toString() {
        return "PortfolioResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", strategy='" + strategy + '\'' +
                ", currency='" + currency + '\'' +
                ", transactions=" + transactions +
                ", dividends=" + dividends +
                '}';
    }*/
}
