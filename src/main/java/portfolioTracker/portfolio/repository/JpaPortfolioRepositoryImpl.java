package portfolioTracker.portfolio.repository;

import portfolioTracker.portfolio.domain.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPortfolioRepositoryImpl extends JpaRepository<PortfolioEntity, Long>, PortfolioRepository {

    @Override
    Boolean existsByName(String name);

    @Override
    Optional<PortfolioEntity> findByName(String name);

    @Override
    @Query(value = "SELECT DISTINCT currency FROM portfolios", nativeQuery = true)
    List<String> findAllPortfolioCurrencies();

    @Override
    @Query(value = "SELECT id, name, strategy, currency, username FROM portfolios WHERE id= :id",
            nativeQuery = true)
    Optional<PortfolioEntity> findByIdSkipEvents(@Param("id") Long id);

    @Override
    @Query(value = "SELECT id, name, strategy, currency, username FROM portfolios",
            nativeQuery = true)
    List<PortfolioEntity> findAllSKipEvents();
}
