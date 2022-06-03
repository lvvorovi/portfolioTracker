package com.portfolioTracker.payload.portfolioSummaryDto.dto.positionSummary.position.event.eventType;

import javax.validation.constraints.NotNull;

public enum EventType {

    BUY("Buy"),
    SELL("Sell"),
    DIVIDEND("Dividend");

    private final String code;

    EventType(@NotNull String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
