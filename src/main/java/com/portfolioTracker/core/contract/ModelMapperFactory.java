package com.portfolioTracker.core.contract;

import com.portfolioTracker.model.dividend.mapper.DividendMapperFactory;
import com.portfolioTracker.model.portfolio.mapper.PortfolioMapperFactory;
import com.portfolioTracker.model.transaction.mapper.TransactionMapperFactory;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface ModelMapperFactory {

    ModelMapperContract getInstance();

    static ModelMapperFactory getFactoryOfType(@NotNull ModelType modelType) {
        switch (modelType) {
            case DIVIDEND:
                return new DividendMapperFactory();
            case PORTFOLIO:
                return new PortfolioMapperFactory();
            case TRANSACTION:
                return new TransactionMapperFactory();
        }

        throw new IllegalArgumentException("Model type " + modelType + " is wrong argument");
    }

    public enum ModelType {
        DIVIDEND, PORTFOLIO, TRANSACTION
    }
}
