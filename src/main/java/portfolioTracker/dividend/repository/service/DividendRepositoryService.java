package portfolioTracker.dividend.repository.service;

import portfolioTracker.dividend.domain.DividendEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DividendRepositoryService {

    DividendEntity save(DividendEntity requestEntity);

    Optional<DividendEntity> findById(String id);

    List<DividendEntity> findAllByTicker(String ticker);

    List<DividendEntity> findAllByPortfolioId(String id);

    List<DividendEntity> findAllByUsername(String username);

    Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

    boolean existsById(String id);

    boolean existsByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

    void deleteById(String id);
}
