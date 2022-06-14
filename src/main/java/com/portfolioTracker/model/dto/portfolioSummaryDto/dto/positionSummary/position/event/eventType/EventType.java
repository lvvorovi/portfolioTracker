package com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
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
