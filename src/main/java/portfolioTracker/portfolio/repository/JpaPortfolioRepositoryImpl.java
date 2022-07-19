package portfolioTracker.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import portfolioTracker.portfolio.domain.PortfolioEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPortfolioRepositoryImpl extends JpaRepository<PortfolioEntity, String>, PortfolioRepository {

    @Override
    @Query(value = "SELECT DISTINCT currency FROM portfolios", nativeQuery = true)
    List<String> findAllPortfolioCurrencies();

    @Override
    @Query(value = "SELECT id, name, strategy, currency, username FROM portfolios WHERE id= :id",
            nativeQuery = true)
    Optional<PortfolioEntity> findById(@Param("id") String id);

    @Override
    @Query(value = "SELECT id, name, strategy, currency, username FROM portfolios WHERE  username = :username",
            nativeQuery = true)
    List<PortfolioEntity> findAllByUsername(@Param("username") String username);
}
