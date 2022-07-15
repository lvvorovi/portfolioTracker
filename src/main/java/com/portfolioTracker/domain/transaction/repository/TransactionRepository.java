package com.portfolioTracker.domain.transaction.repository;

import com.portfolioTracker.domain.transaction.TransactionEntity;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    List<String> findAllUniqueTickers();

    TransactionEntity save(TransactionEntity entity);

    Optional<TransactionEntity> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<TransactionEntity> findAll();

    List<TransactionEntity> findAllByPortfolioId(Long id);
}
