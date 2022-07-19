package portfolioTracker.portfolio.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface PortfolioService {

    @PostAuthorize("returnObject.username == authentication.name")
    PortfolioDtoResponse findById(String id);

    @PreAuthorize("#username == authentication.name")
    List<PortfolioDtoResponse> findAllByUsername(String username);

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse save(PortfolioDtoCreateRequest requestDto);

    @PreAuthorize("#requestDto.username == authentication.name")
    PortfolioDtoResponse update(PortfolioDtoUpdateRequest requestDto);

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    void deleteById(String id);

    boolean isOwner(String id);

    List<String> findAllPortfolioCurrencies();
}
