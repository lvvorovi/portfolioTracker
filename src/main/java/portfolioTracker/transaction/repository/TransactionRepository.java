package portfolioTracker.transaction.repository;

import portfolioTracker.transaction.domain.TransactionEntity;

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
