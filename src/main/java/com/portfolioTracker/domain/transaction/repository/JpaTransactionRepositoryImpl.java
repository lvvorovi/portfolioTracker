package com.portfolioTracker.domain.transaction.repository;

import com.portfolioTracker.domain.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaTransactionRepositoryImpl extends JpaRepository<TransactionEntity, Long>, TransactionRepository {

    @Override
    @Query(value = "SELECT DISTINCT ticker FROM transactions", nativeQuery = true)
    List<String> findAllUniqueTickers();

//    @Override
//    List<TransactionEntity> findAllByPortfolioId(Long portfolioId);
}
