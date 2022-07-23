package portfolioTracker.dividend.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolioTracker.dividend.domain.DividendEntity;

@Repository
public interface JpaDividendRepositoryImpl extends JpaRepository<DividendEntity, String>, DividendRepository {

}