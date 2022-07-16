package portfolioTracker.portfolio;

import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.service.PortfolioService;
import portfolioTracker.transaction.TransactionController;
import portfolioTracker.dividend.DividendController;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService service;

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDtoResponse> findById(@PathVariable String id,
                                                         @Nullable @RequestParam Boolean includeEvents) {
        PortfolioDtoResponse response;
        if (includeEvents == null || !includeEvents) {
            response = service.findByIdSkipEvents(id);
            addSelfRelLink(response);
        } else {
            response = service.findByIdWithEvents(id);
            addSelfRelLinkInclEvents(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDtoResponse>> findAll(@Nullable @RequestParam Boolean includeEvents) {
        List<PortfolioDtoResponse> portfolioResponseList;
        if (includeEvents == null || !includeEvents) {
            portfolioResponseList = service.findAllSkipEvents();
            portfolioResponseList.forEach(this::addSelfRelLink);
        } else {
            portfolioResponseList = service.findAllWithEvents();
            portfolioResponseList.forEach(this::addSelfRelLinkInclEvents);
        }
        return ResponseEntity.ok(portfolioResponseList);
    }

    @PostMapping
    public ResponseEntity<PortfolioDtoResponse> save(@Valid @RequestBody PortfolioDtoCreateRequest requestDto) {
        PortfolioDtoResponse responseDto = service.save(requestDto);
        addSelfRelLink(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PortfolioDtoResponse> update(@Valid @RequestBody PortfolioDtoUpdateRequest requestDto) {
        PortfolioDtoResponse responseDto = service.update(requestDto);
        addSelfRelLink(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void addSelfRelLink(PortfolioDtoResponse responseDto) {
        responseDto
                .add(linkTo(methodOn(PortfolioController.class)
                        .findById(responseDto.getId(), null))
                        .withSelfRel());
    }

    private void addSelfRelLinkInclEvents(PortfolioDtoResponse responseDto) {
        addSelfRelLink(responseDto);

        responseDto.getDividendList().forEach(dto ->
                dto.add(linkTo(methodOn(DividendController.class)
                        .findById(dto.getId())).withSelfRel()));

        responseDto.getTransactionList().forEach(dto ->
                dto.add(WebMvcLinkBuilder.linkTo(methodOn(TransactionController.class)
                        .findById(dto.getId())).withSelfRel()));
    }

}
