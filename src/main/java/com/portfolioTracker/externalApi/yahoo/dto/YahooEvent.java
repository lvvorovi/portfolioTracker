package com.portfolioTracker.externalApi.yahoo.dto;

import com.portfolioTracker.validation.annotation.AmountOfMoney;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Quantity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class YahooEvent {

    @AmountOfMoney
    private BigDecimal amount;
    @Date
    private LocalDate date;
    @NotEmpty
    private String type;
    @NotEmpty
    private String data;
    @Quantity
    private BigDecimal numerator;
    @Quantity
    private BigDecimal denominator;
    @NotEmpty
    private String splitRatio;

/*    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }*/

    public void setDate(String date) {
        LocalDate yahooDayZeroDate = LocalDate.of(1970, 1, 1);
        this.date = yahooDayZeroDate.plusDays(TimeUnit.SECONDS.toDays(Long.parseLong(date)));
    }
/*
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigDecimal getNumerator() {
        return numerator;
    }

    public void setNumerator(BigDecimal numerator) {
        this.numerator = numerator;
    }

    public BigDecimal getDenominator() {
        return denominator;
    }

    public void setDenominator(BigDecimal denominator) {
        this.denominator = denominator;
    }

    public String getSplitRatio() {
        return splitRatio;
    }

    public void setSplitRatio(String splitRatio) {
        this.splitRatio = splitRatio;
    }*/

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YahooEvent that = (YahooEvent) o;
        return Objects.equals(amount, that.amount) && Objects.equals(date, that.date) && Objects.equals(type, that.type) && Objects.equals(data, that.data) && Objects.equals(numerator, that.numerator) && Objects.equals(denominator, that.denominator) && Objects.equals(splitRatio, that.splitRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, date, type, data, numerator, denominator, splitRatio);
    }*/
/*
    @Override
    public String toString() {
        return "YahooEvent{" +
                "amount=" + amount +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", numerator=" + numerator +
                ", denominator=" + denominator +
                ", splitRatio='" + splitRatio + '\'' +
                '}';
    }*/
}
