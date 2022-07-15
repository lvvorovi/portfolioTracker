package portfolioTracker.transaction.mapper;

import portfolioTracker.core.contract.ModelMapperFactory;

public class TransactionMapperFactory implements ModelMapperFactory {

    public TransactionMapper getInstance() {
        return new CustomTransactionMapper();
    }

}
