package com.portfolioTracker.summaryModule.event;

import com.portfolioTracker.summaryModule.event.eventType.EventType;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.validation.exception.IncorrectTransactionTypeForMethodException;

import java.math.BigDecimal;

public class EventUtil {

    public static BigDecimal getSoldFromTransaction(TransactionDtoResponse transaction) {
        if (!transaction.getType().equals(EventType.SELL)) throw new
                IncorrectTransactionTypeForMethodException(EventUtil.class +
                " getSoldFromTransaction() only accepts 'EventType.SELL' transactions. " +
                "Provided transaction type is: " + transaction.getType());
        return transaction.getPrice().multiply(transaction.getShares());
    }

    public static BigDecimal getBoughtFromTransaction(TransactionDtoResponse transaction) {
        if (!transaction.getType().equals(EventType.BUY)) throw new
                IncorrectTransactionTypeForMethodException(EventUtil.class +
                " getSoldFromTransaction() only accepts 'EventType.SELL' transactions. " +
                "Provided transaction type is: " + transaction.getType());
        return transaction.getPrice().multiply(transaction.getShares());
    }

}
