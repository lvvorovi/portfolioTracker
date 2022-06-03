package com.portfolioTracker.model.portfolio.repository;

import com.portfolioTracker.model.portfolio.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    Boolean existsByName(@NotNull String name);

    Optional<PortfolioEntity> findByName(@NotNull String name);

    @Query(value = "SELECT DISTINCT currency FROM portfolios", nativeQuery = true)
    List<String> findAllPortfolioCurrencies();

}
