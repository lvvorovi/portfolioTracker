package com.portfolioTracker.domain.transaction.mapper;

import com.portfolioTracker.core.contract.ModelMapperFactory;

public class TransactionMapperFactory implements ModelMapperFactory {

    public TransactionMapper getInstance() {
        return new CustomTransactionMapper();
    }

}
