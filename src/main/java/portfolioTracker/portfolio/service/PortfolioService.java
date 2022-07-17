package portfolioTracker.portfolio.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

import java.util.ArrayList;
import java.util.List;

public interface PortfolioService {

    @PostAuthorize("returnObject.username == authentication.name")
    PortfolioDtoResponse findByIdSkipEvents(String id);

    @PostAuthorize("returnObject.username == authentication.name")
    PortfolioDtoResponse findByIdWithEvents(String id);

    @PostFilter("filterObject.username == authentication.name")
    ArrayList<PortfolioDtoResponse> findAllWithEvents();

    @PostFilter("filterObject.username == authentication.name")
    ArrayList<PortfolioDtoResponse> findAllSkipEvents();

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse save(PortfolioDtoCreateRequest requestDto);

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto);

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    void deleteById(String id);

    boolean isOwner(String id);

    List<String> findAllPortfolioCurrencies();
}
