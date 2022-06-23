package com.portfolioTracker.model.transaction.mapper;

import com.portfolioTracker.core.contract.ModelMapperContract;
import com.portfolioTracker.core.contract.ModelMapperFactory;
import com.portfolioTracker.model.transaction.TransactionEntity;
import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;

public class TransactionMapperFactory implements ModelMapperFactory {

    public ModelMapperContract<TransactionEntity, TransactionRequestDto, TransactionResponseDto> getInstance() {
        return new CustomTransactionMapper();
    }

}
