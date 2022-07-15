package com.portfolioTracker.domain.dividend.repository;

import com.portfolioTracker.domain.dividend.DividendEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DividendRepository {

    Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, Long portfolioId);

    List<DividendEntity> findAllByTicker(String ticker);

    boolean existsByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, Long portfolioId);

    DividendEntity save(DividendEntity toEntity);

    List<DividendEntity> findAll();

    Optional<DividendEntity> findById(Long id);

    List<DividendEntity> findAllByPortfolioId(Long id);

    void deleteById(Long id);

    void deleteAll();

    boolean existsById(Long id);
}
