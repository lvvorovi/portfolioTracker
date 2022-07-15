package portfolioTracker.portfolio.mapper;

import portfolioTracker.core.contract.DomainMapper;
import portfolioTracker.core.contract.ModelMapperFactory;

public class PortfolioMapperFactory implements ModelMapperFactory {

    public DomainMapper getInstance() {
        return new CustomPortfolioMapper();
    }

}
