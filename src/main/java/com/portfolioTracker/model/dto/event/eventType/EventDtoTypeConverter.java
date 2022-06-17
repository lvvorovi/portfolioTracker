package com.portfolioTracker.model.dto.event.eventType;

import org.springframework.validation.annotation.Validated;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

@Validated
@Converter(autoApply = true)
public class EventDtoTypeConverter implements AttributeConverter<EventType, String> {

    @Override
    public String convertToDatabaseColumn(@NotNull EventType eventType) {
        if (eventType == null) {
            return null;
        }

        return eventType.getCode();
    }

    @Override
    public EventType convertToEntityAttribute(@NotEmpty String dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(EventType.values())
                .filter(value -> value.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("EventDtoTypeConverter failed to convert DB data " +
                        dbData + " to a EventType value"));
    }
}
