package com.portfolioTracker.domain.portfolio.repository;

import com.portfolioTracker.domain.portfolio.PortfolioEntity;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository {

    Boolean existsByName(String name);

    Optional<PortfolioEntity> findByName(String name);

    List<String> findAllPortfolioCurrencies();

    PortfolioEntity save(PortfolioEntity toEntity);

    List<PortfolioEntity> save(List<PortfolioEntity> entityList);

    List<PortfolioEntity> findAll();

    Optional<PortfolioEntity> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);
}
