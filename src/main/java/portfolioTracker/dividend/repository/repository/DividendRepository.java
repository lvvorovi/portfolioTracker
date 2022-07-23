package portfolioTracker.dividend.repository.repository;

import portfolioTracker.dividend.repository.service.DividendRepositoryService;
import portfolioTracker.dividend.domain.DividendEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DividendRepository extends DividendRepositoryService {

    @Override
    DividendEntity save(DividendEntity requestEntity);

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

}
