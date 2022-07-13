package com.portfolioTracker.domain.transaction.service;

import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.List;

public interface TransactionService {

    @PreAuthorize("#requestDto.username == authentication.name " +
            " && " +
            "@portfolioServiceImpl.isOwner(#requestDto.portfolioId)")
    TransactionDtoResponse save(TransactionDtoCreateRequest requestDto);

    @PreFilter("filterObject.username == authentication.name" +
            " && " +
            "@portfolioServiceImpl.isOwner(filterObject.portfolioId)")
    List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> requestDtoList);

    @PostAuthorize("returnObject.username == authentication.name")
    TransactionDtoResponse findById(Long id);

    @PostFilter("filterObject.username == authentication.name")
    List<TransactionDtoResponse> findAll();

    Boolean existsById(Long id);

    List<String> findAllUniqueTickers();

    @PreAuthorize("@portfolioServiceImpl.isOwner(#id)")
    List<TransactionDtoResponse> findByPortfolioId(Long id);

    @PreAuthorize("#requestDto.username == authentication.name" +
            " && " +
            "@portfolioServiceImpl.isOwner(#requestDto.portfolioId)" +
            " && " +
            "@transactionServiceImpl.isOwner(#requestDto.id)")
    TransactionDtoResponse update(TransactionDtoUpdateRequest requestDto);

    @PreAuthorize("@transactionServiceImpl.isOwner(#id)")
    void deleteById(Long id);

    boolean isOwner(Long id);
}
