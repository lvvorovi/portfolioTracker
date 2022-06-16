package com.portfolioTracker.model.dividend.repository;

import com.portfolioTracker.model.dividend.DividendEntity;
import com.portfolioTracker.validation.annotation.Date;
import com.portfolioTracker.validation.annotation.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Validated
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    Optional<DividendEntity> findByTickerAndExDate(@Ticker String ticker, @Date LocalDate exDate);

    List<DividendEntity> findAllByTicker(@Ticker String ticker);

}
