package portfolioTracker.dividend.service;

import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.List;

public interface DividendService {

    @PreAuthorize("#requestDto.username == authentication.name" +
            " && " +
            "@portfolioServiceImpl.isOwner(#requestDto.portfolioId)")
    DividendDtoResponse save(DividendDtoCreateRequest requestDto);

    @PreFilter("filterObject.username == authentication.name" +
            " && " +
            "@portfolioServiceImpl.isOwner(filterObject.portfolioId)")
    List<DividendDtoResponse> saveAll(List<DividendDtoCreateRequest> requestDtoList);

    @PreAuthorize("#username == authentication.name")
    List<DividendDtoResponse> findAllByUsername(String username);

    @PostAuthorize("returnObject.username == authentication.name")
    DividendDtoResponse findById(String id);

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    List<DividendDtoResponse> findAllByPortfolioId(String id);

    @PreAuthorize("#requestDto.username == authentication.name" +
            " && " +
            "@portfolioServiceImpl.isOwner(#requestDto.portfolioId)")
    DividendDtoResponse update(DividendDtoUpdateRequest requestDto);

    @PreAuthorize("@dividendServiceImpl.isOwner(#id)")
    void deleteById(String id);

    boolean isOwner(String id);
}
