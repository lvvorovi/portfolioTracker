package com.portfolioTracker.core.contract;

import com.portfolioTracker.domain.dividend.mapper.DividendMapperFactory;
import com.portfolioTracker.domain.portfolio.mapper.PortfolioMapperFactory;
import com.portfolioTracker.domain.transaction.mapper.TransactionMapperFactory;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ModelMapperFactory {

    static ModelMapperFactory getFactoryOfType(ModelType modelType) {
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

    DomainMapper getInstance();

    public enum ModelType {
        DIVIDEND, PORTFOLIO, TRANSACTION
    }
}
