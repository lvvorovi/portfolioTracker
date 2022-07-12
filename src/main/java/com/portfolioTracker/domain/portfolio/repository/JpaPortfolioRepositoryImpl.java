package com.portfolioTracker.domain.portfolio.repository;

import com.portfolioTracker.domain.portfolio.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPortfolioRepositoryImpl extends JpaRepository<PortfolioEntity, Long>, PortfolioRepository {

    @Override
    Boolean existsByName(String name);

    @Override
    Optional<PortfolioEntity> findByName(String name);

    @Override
    @Query(value = "SELECT DISTINCT currency FROM portfolios", nativeQuery = true)
    List<String> findAllPortfolioCurrencies();

}
