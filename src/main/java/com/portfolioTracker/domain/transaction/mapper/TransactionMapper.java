package com.portfolioTracker.domain.transaction.mapper;

import com.portfolioTracker.core.contract.DomainMapper;
import com.portfolioTracker.domain.transaction.TransactionEntity;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface TransactionMapper extends DomainMapper<TransactionEntity,TransactionDtoCreateRequest, TransactionDtoUpdateRequest, TransactionDtoResponse> {

    @Override
    @Valid TransactionEntity updateToEntity(TransactionDtoUpdateRequest dto);

    @Override
    @Valid TransactionEntity createToEntity(TransactionDtoCreateRequest dto);

    @Override
    @Valid TransactionDtoResponse toDto(TransactionEntity entity);

}
