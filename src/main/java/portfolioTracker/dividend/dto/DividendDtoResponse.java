package portfolioTracker.dividend.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import portfolioTracker.dto.eventType.EventType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DividendDtoResponse extends RepresentationModel<DividendDtoResponse> {

    private String id;
    private String ticker;
    private LocalDate exDate;
    private LocalDate date;
    private BigDecimal amount;
    private EventType type;
    private String portfolioId;
    private String username;

}
