package portfolioTracker.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import portfolioTracker.transaction.domain.TransactionEntity;

import java.util.List;

public interface JpaTransactionRepositoryImpl extends JpaRepository<TransactionEntity, String>, TransactionRepository {

    @Override
    @Query(value = "SELECT DISTINCT ticker FROM transactions", nativeQuery = true)
    List<String> findAllUniqueTickers();

}
