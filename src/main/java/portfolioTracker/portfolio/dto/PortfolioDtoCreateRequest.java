package portfolioTracker.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import portfolioTracker.core.validation.annotation.Currency;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDtoCreateRequest {

    @NotBlank
    @Size(min = 3, max = 50, message = "length 3 to 50 characters")
    private String name;
    @NotBlank
    @Size(min = 3, max = 150, message = "length 3 to 150 characters")
    private String strategy;
    @Currency
    private String currency;
    @NotBlank
    @Size(max = 50, message = "max 50 characters")
    private String username;

}