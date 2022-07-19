package portfolioTracker.dto.portfolioWithEventsDto;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;

import java.util.List;

public interface PortfolioWithEventsService {

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    PortfolioDtoResponse findById(String portfolioId);

    @PreAuthorize("#username == authentication.name")
    List<PortfolioDtoResponse> findAllByUsername(String username);
}
