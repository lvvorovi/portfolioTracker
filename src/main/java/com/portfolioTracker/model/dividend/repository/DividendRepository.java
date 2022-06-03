package com.portfolioTracker.model.dividend.repository;

import com.portfolioTracker.model.dividend.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    Optional<DividendEntity> findByTickerAndExDate(@NotNull String ticker, @NotNull LocalDate exDate);

    List<DividendEntity> findAllByTicker(@NotNull String ticker);

}
