package portfolioTracker.dividend.repository;

import portfolioTracker.dividend.domain.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaDividendRepositoryImpl extends JpaRepository<DividendEntity, String>, DividendRepository {

    @Override
    Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

    @Override
    List<DividendEntity> findAllByTicker(String ticker);

    @Override
    boolean existsByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

}