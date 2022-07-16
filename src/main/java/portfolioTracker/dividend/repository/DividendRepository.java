package portfolioTracker.dividend.repository;

import portfolioTracker.dividend.domain.DividendEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DividendRepository {

    Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

    List<DividendEntity> findAllByTicker(String ticker);

    boolean existsByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

    DividendEntity save(DividendEntity toEntity);

    List<DividendEntity> findAllByUsername(String username);

    Optional<DividendEntity> findById(String id);

    List<DividendEntity> findAllByPortfolioId(String id);

    void deleteById(String id);

    boolean existsById(String id);
}
