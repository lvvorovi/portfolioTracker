package portfolioTracker.portfolio.repository;

import com.jayway.jsonpath.JsonPath;
import portfolioTracker.portfolio.domain.PortfolioEntity;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository {

    Boolean existsByName(String name);

    Optional<PortfolioEntity> findByNameAndUsername(String name, String username);

    List<String> findAllPortfolioCurrencies();

    PortfolioEntity save(PortfolioEntity toEntity);

    List<PortfolioEntity> save(List<PortfolioEntity> entityList);

    void deleteById(String id);

    Optional<PortfolioEntity> findById(String id);

    List<PortfolioEntity> findAllByUsername(String username);

    Optional<PortfolioEntity> findByIdIncludeEvents(String id);

    List<PortfolioEntity> findAllByUsernameIncludeEvents(String username);
}
