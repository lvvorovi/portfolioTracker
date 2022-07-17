package portfolioTracker.transaction.repository;

import portfolioTracker.transaction.domain.TransactionEntity;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    List<String> findAllUniqueTickers();

    TransactionEntity save(TransactionEntity entity);

    Optional<TransactionEntity> findById(String id);

    void deleteById(String id);

    boolean existsById(String id);

    List<TransactionEntity> findAllByUsername(String username);

    List<TransactionEntity> findAllByPortfolioId(String id);
}
