package portfolioTracker.portfolio.repository;

import portfolioTracker.portfolio.domain.PortfolioEntity;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository {

    Boolean existsByName(String name);

    Optional<PortfolioEntity> findByName(String name);

    List<String> findAllPortfolioCurrencies();

    PortfolioEntity save(PortfolioEntity toEntity);

    List<PortfolioEntity> save(List<PortfolioEntity> entityList);

    List<PortfolioEntity> findAll();

    Optional<PortfolioEntity> findById(String id);

    void deleteById(String id);

    Optional<PortfolioEntity> findByIdSkipEvents(String id);

    List<PortfolioEntity> findAllSKipEvents();
}
