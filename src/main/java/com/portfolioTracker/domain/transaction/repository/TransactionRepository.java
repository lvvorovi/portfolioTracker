package com.portfolioTracker.domain.transaction.repository;

import com.portfolioTracker.domain.transaction.TransactionEntity;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    List<String> findAllUniqueTickers();

    TransactionEntity save(TransactionEntity entity);

//    Iterable<TransactionEntity> saveAll(Iterable<TransactionEntity> entityList);

    List<TransactionEntity> findAllByUsername(String username);

    Optional<TransactionEntity> findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    boolean existsById(Long id);

    List<TransactionEntity> findAllByPortfolioId(Long portfolioId);
}
