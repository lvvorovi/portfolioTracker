package portfolioTracker.portfolio;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolioTracker.core.LinkUtil;
import portfolioTracker.dto.portfolioWithEventsDto.PortfolioWithEventsService;
import portfolioTracker.portfolio.dto.PortfolioDtoCreateRequest;
import portfolioTracker.portfolio.dto.PortfolioDtoResponse;
import portfolioTracker.portfolio.dto.PortfolioDtoUpdateRequest;
import portfolioTracker.portfolio.service.PortfolioService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PortfolioWithEventsService portfolioWithEventsService;
    private final LinkUtil linkUtil;

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDtoResponse> findById(@PathVariable String id,
                                                         @Nullable @RequestParam Boolean includeEvents) {
        PortfolioDtoResponse response;
        if (includeEvents == null || !includeEvents) {
            response = portfolioService.findById(id);
            linkUtil.addLinks(response);
        } else {
            response = portfolioWithEventsService.findById(id);
            linkUtil.addLinks(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDtoResponse>> findAllByUsername(@Nullable @RequestParam Boolean includeEvents,
                                                                        Authentication authentication) {
        List<PortfolioDtoResponse> portfolioResponseList;
        if (includeEvents == null || !includeEvents) {
            portfolioResponseList = portfolioService.findAllByUsername(authentication.getName());
            portfolioResponseList.forEach(linkUtil::addLinks);
        } else {
            portfolioResponseList = portfolioWithEventsService.findAllByUsername(authentication.getName());
            portfolioResponseList.forEach(linkUtil::addLinksInclEvents);
        }
        return ResponseEntity.ok(portfolioResponseList);
    }

    @PostMapping
    public ResponseEntity<PortfolioDtoResponse> save(@Valid @RequestBody PortfolioDtoCreateRequest requestDto) {
        PortfolioDtoResponse responseDto = portfolioService.save(requestDto);
        linkUtil.addLinks(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PortfolioDtoResponse> update(@Valid @RequestBody PortfolioDtoUpdateRequest requestDto) {
        PortfolioDtoResponse responseDto = portfolioService.update(requestDto);
        linkUtil.addLinks(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        portfolioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
