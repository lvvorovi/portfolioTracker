package com.portfolioTracker.model.dto.portfolioSummaryDto.dto.positionSummary.position.event.eventType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class EventTypeConverter implements AttributeConverter<EventType, String> {

    @Override
    public String convertToDatabaseColumn(EventType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public EventType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(EventType.values())
                .filter(value -> value.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("EventTypeConverter failed to convert DB data " +
                        dbData + " to a EventType value"));
    }
}
