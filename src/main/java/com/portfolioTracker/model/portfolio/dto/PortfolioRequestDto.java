package com.portfolioTracker.model.portfolio.dto;

import com.portfolioTracker.contract.Currency;
import com.portfolioTracker.contract.ModelName;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Validated
public class PortfolioRequestDto {

    private Long id;
    @ModelName
    private String name;
    @NotEmpty
    private String strategy;
    @Currency
    private String currency;

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
        this.currency = currency.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioRequestDto that = (PortfolioRequestDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(strategy, that.strategy) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, strategy, currency);
    }

    @Override
    public String toString() {
        return "PortfolioRequestDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", strategy='" + strategy + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
