package portfolioTracker.core.contract;

import org.springframework.validation.annotation.Validated;
import portfolioTracker.dividend.mapper.DividendMapperFactory;
import portfolioTracker.portfolio.mapper.PortfolioMapperFactory;
import portfolioTracker.transaction.mapper.TransactionMapperFactory;

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
