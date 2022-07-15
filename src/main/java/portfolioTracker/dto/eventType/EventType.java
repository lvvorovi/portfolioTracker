package portfolioTracker.dto.eventType;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public enum EventType {

    BUY("BUY"),
    SELL("SELL"),
    DIVIDEND("DIVIDEND");

    private final String code;

    EventType(@NotEmpty String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
