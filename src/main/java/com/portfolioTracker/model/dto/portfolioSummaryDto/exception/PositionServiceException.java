package com.portfolioTracker.model.dto.portfolioSummaryDto.exception;

import com.portfolioTracker.contract.ValidationException;

public class PositionServiceException extends ValidationException {

    public PositionServiceException(String message) {
        super(message);
    }
}
