package portfolioTracker.dividend;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolioTracker.core.LinkUtil;
import portfolioTracker.core.ValidList;
import portfolioTracker.dividend.dto.DividendDtoCreateRequest;
import portfolioTracker.dividend.dto.DividendDtoResponse;
import portfolioTracker.dividend.dto.DividendDtoUpdateRequest;
import portfolioTracker.dividend.service.DividendService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/dividends")
public class DividendController {

    private final DividendService service;
    private final LinkUtil linkUtil;

    @GetMapping("/{id}")
    public ResponseEntity<DividendDtoResponse> findById(@PathVariable String id) {
        DividendDtoResponse responseDto = service.findById(id);
        linkUtil.addLinks(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<DividendDtoResponse>> findAll(@Nullable @RequestParam String portfolioId,
                                                             Authentication authentication) {
        List<DividendDtoResponse> responseDtoList;
        if (portfolioId == null) {
            responseDtoList = service.findAllByUsername(authentication.getName());
            responseDtoList.forEach(linkUtil::addLinks);
        } else {
            responseDtoList = service.findAllByPortfolioId(portfolioId);
            responseDtoList.forEach(linkUtil::addLinks);
        }
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<DividendDtoResponse> save(@Valid @RequestBody DividendDtoCreateRequest requestDto) {
        DividendDtoResponse responseDto = service.save(requestDto);
        linkUtil.addLinks(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<DividendDtoResponse>> saveAll(
            @RequestBody @Valid ValidList<DividendDtoCreateRequest> requestDtoList) {
        List<DividendDtoResponse> responseDtoList = service.saveAll(requestDtoList);
        responseDtoList.forEach(linkUtil::addLinks);
        return new ResponseEntity<>(responseDtoList, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<DividendDtoResponse> update(@Valid @RequestBody DividendDtoUpdateRequest requestDto) {
        DividendDtoResponse responseDto = service.update(requestDto);
        linkUtil.addLinks(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
