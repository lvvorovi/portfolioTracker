package portfolioTracker.dividend.repository.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import portfolioTracker.dividend.repository.cache.DividendCache;
import portfolioTracker.dividend.repository.repository.DividendRepository;
import portfolioTracker.dividend.domain.DividendEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Primary
@AllArgsConstructor
public class DividendRepositoryServiceImpl implements DividendRepositoryService {

    private final DividendRepository repository;
    private final DividendCache cache;

    @Override
    public DividendEntity save(DividendEntity requestEntity) {
        DividendEntity savedEntity = repository.save(requestEntity);
        cache.save(savedEntity);
        return savedEntity;
    }

    @Override
    public Optional<DividendEntity> findById(String id) {
        Optional<DividendEntity> optionalEntity = cache.findById(id);
        if (optionalEntity.isEmpty()) optionalEntity = repository.findById(id);
        return optionalEntity;
    }

    @Override
    public List<DividendEntity> findAllByTicker(String ticker) {
        var entityList = cache.findAllByTicker(ticker);
        if (entityList.isEmpty()) entityList = repository.findAllByTicker(ticker);
        return entityList;
    }

    @Override
    public List<DividendEntity> findAllByPortfolioId(String id) {
        var entityList = cache.findAllByPortfolioId(id);
        if (entityList.isEmpty()) entityList = repository.findAllByPortfolioId(id);
        return entityList;
    }

    @Override
    public List<DividendEntity> findAllByUsername(String username) {
        var entitytList = cache.findAllByUsername(username);
        if (entitytList.isEmpty()) entitytList = repository.findAllByUsername(username);
        return entitytList;
    }

    @Override
    public Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId) {
        var optionalEntity = cache.findByTickerAndExDateAndPortfolioId(ticker, exDate, portfolioId);
        if (optionalEntity.isEmpty()) optionalEntity = repository
                .findByTickerAndExDateAndPortfolioId(ticker, exDate, portfolioId);
        return optionalEntity;
    }

    @Override
    public boolean existsById(String id) {
        boolean exists = cache.existsById(id);
        if (!exists) exists = repository.existsById(id);
        return exists;
    }

    @Override
    public boolean existsByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, String portfolioId) {
        var exists = cache.existsByTickerAndExDateAndPortfolioId(ticker, exDate, portfolioId);
        if (!exists) exists = repository.existsByTickerAndExDateAndPortfolioId(ticker, exDate, portfolioId);
        return exists;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
        cache.deleteById(id);
    }
}
