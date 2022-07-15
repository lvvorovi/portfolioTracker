package portfolioTracker.transaction.repository;

import portfolioTracker.transaction.domain.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaTransactionRepositoryImpl extends JpaRepository<TransactionEntity, Long>, TransactionRepository {

    @Override
    @Query(value = "SELECT DISTINCT ticker FROM transactions", nativeQuery = true)
    List<String> findAllUniqueTickers();

}
