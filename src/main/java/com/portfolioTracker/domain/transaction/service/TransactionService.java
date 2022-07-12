package com.portfolioTracker.domain.transaction.service;

import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;

import java.util.List;

public interface TransactionService {

    TransactionDtoResponse save(TransactionDtoCreateRequest dto);

    List<TransactionDtoResponse> saveAll(List<TransactionDtoCreateRequest> dtoList);

    List<TransactionDtoResponse> findAll();

    TransactionDtoResponse findById(Long id);

    void deleteById(Long id);

    void deleteAll();

    TransactionDtoResponse update(TransactionDtoUpdateRequest dtoRequest);

    Boolean existsById(Long id);

    List<String> findAllUniqueTickers();
}