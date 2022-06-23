package com.portfolioTracker.model.portfolio.repository;

import com.portfolioTracker.model.portfolio.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Validated
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    Boolean existsByName(@NotBlank String name);

    Optional<PortfolioEntity> findByName(@NotBlank String name);

    @Query(value = "SELECT DISTINCT currency FROM portfolios", nativeQuery = true)
    List<String> findAllPortfolioCurrencies();

}
