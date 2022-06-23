package com.portfolioTracker.model.dividend.repository;

import com.portfolioTracker.model.dividend.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Validated
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    Optional<DividendEntity> findByTickerAndExDateAndPortfolioId(String ticker, LocalDate exDate, Long portfolioId);

    List<DividendEntity> findAllByTicker(String ticker);

}
