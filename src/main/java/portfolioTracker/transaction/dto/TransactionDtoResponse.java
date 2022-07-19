package portfolioTracker.transaction.dto;

import lombok.*;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;
import portfolioTracker.dto.eventType.EventType;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDtoResponse extends RepresentationModel<TransactionDtoResponse> {

    private String id;
    private String ticker;
    private LocalDate date;
    private BigDecimal price;
    private BigDecimal shares;
    private BigDecimal commission;
    private EventType type;
    private String portfolioId;
    private String username;
}
