package com.portfolioTracker.model.transaction.repository;

import com.portfolioTracker.model.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = "SELECT DISTINCT ticker FROM transactions", nativeQuery = true)
    List<String> findAllUniqueTickers();

}
