package portfolioTracker.dto.eventType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

import static portfolioTracker.core.ExceptionErrors.EVENTDTO_CONVERTER_FAIL_EXCEPTION_MESSAGE;

@Converter(autoApply = true)
public class EventDtoTypeConverter implements AttributeConverter<EventType, String> {

    @Override
    public String convertToDatabaseColumn(EventType eventType) {
        if (eventType == null) {
            return null;
        }
        return eventType.getCode();
    }

    @Override
    public EventType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Stream.of(EventType.values())
                .filter(value -> value.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(EVENTDTO_CONVERTER_FAIL_EXCEPTION_MESSAGE));
    }
}
