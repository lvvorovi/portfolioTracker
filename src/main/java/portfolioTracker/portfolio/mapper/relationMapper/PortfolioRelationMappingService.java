package portfolioTracker.portfolio.mapper.relationMapper;

import portfolioTracker.dividend.mapper.DividendMapper;
import portfolioTracker.portfolio.mapper.PortfolioMapper;
import portfolioTracker.transaction.mapper.TransactionMapper;

public interface PortfolioRelationMappingService extends PortfolioMapper, DividendMapper, TransactionMapper {
}
