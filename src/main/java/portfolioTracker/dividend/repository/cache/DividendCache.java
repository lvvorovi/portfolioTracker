package portfolioTracker.dividend.repository.cache;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import portfolioTracker.dividend.repository.service.DividendRepositoryService;
import portfolioTracker.dividend.domain.DividendEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DividendCache extends DividendRepositoryService {

    @Override
    Optional<DividendEntity> findById(String id);

    @Override
    List<DividendEntity> findAllByTicker(String ticker);

    @Override
    List<DividendEntity> findAllByPortfolioId(String id);

    @Override
    List<DividendEntity> findAllByUsername(String username);

    @Override
    Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

    @Override
    boolean existsById(String id);

    @Override
    boolean existsByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId);

    @Override
    void deleteById(String id);

    @Async
    @Scheduled(fixedRateString = "PT60M")
    void removeTtlOutdated();
}
