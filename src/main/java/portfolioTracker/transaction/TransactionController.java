package portfolioTracker.transaction;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import portfolioTracker.core.LinkUtil;
import portfolioTracker.core.ValidList;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.service.TransactionService;

import javax.validation.Valid;
import java.util.List;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService service;
    private final LinkUtil linkUtil;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDtoResponse> findById(@PathVariable String id) {
        TransactionDtoResponse responseDto = service.findById(id);
        linkUtil.addLinks(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDtoResponse>> findAll(@Nullable @RequestParam String portfolioId,
                                                                Authentication authentication) {
        List<TransactionDtoResponse> responseDtoList;
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
    public ResponseEntity<TransactionDtoResponse> save(@Valid @RequestBody TransactionDtoCreateRequest requestDto) {
        TransactionDtoResponse responseDto = service.save(requestDto);
        linkUtil.addLinks(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<TransactionDtoResponse>> saveAll(
            @Valid @RequestBody ValidList<TransactionDtoCreateRequest> requestDtoList) {
        List<TransactionDtoResponse> responseDtoList = service.saveAll(requestDtoList);
        responseDtoList.forEach(linkUtil::addLinks);
        return new ResponseEntity<>(responseDtoList, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransactionDtoResponse> update(@Valid @RequestBody TransactionDtoUpdateRequest requestDto) {
        TransactionDtoResponse responseDto = service.update(requestDto);
        linkUtil.addLinks(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NumberFormat @PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
