package portfolioTracker.portfolio;

import portfolioTracker.portfolio.domain.PortfolioEntity;

import java.util.UUID;

import static portfolioTracker.util.DividendTestUtil.newDividendEntityList;

public class PortfolioTestUtil {

    public static PortfolioEntity newPortfolioEntitySkipEvents() {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDividendEntityList(null);
        entity.setTransactionEntityList(null);
        entity.setUsername("john@email.com");
        entity.setCurrency("EUR");
        entity.setName("portfolioName");
        entity.setStrategy("portfolioStrategy");
        return entity;
    }

    public static PortfolioEntity newPortfolioEntity() {
        PortfolioEntity entity = new PortfolioEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setDividendEntityList(newDividendEntityList());
        entity.setTransactionEntityList(null);
        entity.setUsername("john@email.com");
        entity.setCurrency("EUR");
        entity.setName("portfolioName");
        entity.setStrategy("portfolioStrategy");
        return entity;
    }


}
