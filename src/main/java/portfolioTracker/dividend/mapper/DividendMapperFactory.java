package portfolioTracker.dividend.mapper;

import portfolioTracker.core.contract.ModelMapperFactory;

public class DividendMapperFactory implements ModelMapperFactory {

    public DividendMapper getInstance() {
        return new CustomDividendMapper();
    }
}
