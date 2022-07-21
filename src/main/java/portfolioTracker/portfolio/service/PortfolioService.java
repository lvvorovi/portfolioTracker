package portfolioTracker.portfolio.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

import java.util.List;

public interface PortfolioService {

    @PostAuthorize("returnObject.username == authentication.name")
    PortfolioDtoResponse findById(String id);

    @PostAuthorize("returnObject.username == authentication.name")
    PortfolioDtoResponse findByIdIncludeEvents(String id);

    @PreAuthorize("#username == authentication.name")
    List<PortfolioDtoResponse> findAllByUsername(String username);

    @PreAuthorize("#username == authentication.name")
    List<PortfolioDtoResponse> findAllByUsernameIncludeEvents(String username);

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse save(PortfolioDtoCreateRequest requestDto);

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto);

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    void deleteById(String id);

    boolean isOwner(String id);

    List<String> findAllPortfolioCurrencies();
}
