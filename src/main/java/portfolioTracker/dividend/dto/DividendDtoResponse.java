package portfolioTracker.dividend.dto;

import lombok.*;
import portfolioTracker.dto.eventType.EventType;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
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
