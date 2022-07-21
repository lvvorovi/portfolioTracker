package portfolioTracker.transaction.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface TransactionService {

    @PreAuthorize("#requestDto.username == authentication.name " +
            " && " +
            "@portfolioServiceImpl.isOwner(#requestDto.portfolioId)")
    @Valid TransactionDtoResponse save(TransactionDtoCreateRequest requestDto);

    @PreFilter("filterObject.username == authentication.name" +
            " && " +
            "@portfolioServiceImpl.isOwner(filterObject.portfolioId)")
    @Valid List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> requestDtoList);

    @PostAuthorize("returnObject.username == authentication.name")
    @Valid TransactionDtoResponse findById(String id);

    @PreAuthorize("#username == authentication.name")
    @Valid List<TransactionDtoResponse> findAllByUsername(String username);

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    @Valid List<TransactionDtoResponse> findAllByPortfolioId(String id);

    List<String> findAllUniqueTickers();

    @PreAuthorize("#requestDto.username == authentication.name" +
            " && " +
            "@portfolioServiceImpl.isOwner(#requestDto.portfolioId)" +
            " && " +
            "@transactionServiceImpl.isOwner(#requestDto.id)")
    @Valid TransactionDtoResponse update(TransactionDtoUpdateRequest requestDto);

    @PreAuthorize("@transactionServiceImpl.isOwner(#id)")
    void deleteById(String id);

    boolean isOwner(String id);
}