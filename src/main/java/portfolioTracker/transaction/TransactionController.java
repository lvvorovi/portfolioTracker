package portfolioTracker.transaction;

import portfolioTracker.core.ValidList;
import portfolioTracker.transaction.dto.TransactionDtoCreateRequest;
import portfolioTracker.transaction.dto.TransactionDtoResponse;
import portfolioTracker.transaction.dto.TransactionDtoUpdateRequest;
import portfolioTracker.transaction.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
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
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService service;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDtoResponse> findById(@PathVariable String id) {
        TransactionDtoResponse responseDto = service.findById(id);
        addSelfRefToJson(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDtoResponse>> findAll(@Nullable @RequestParam String portfolioId) {
        List<TransactionDtoResponse> responseDtoList;
        if (portfolioId == null) {
            responseDtoList = service.findAll();
            responseDtoList.forEach(this::addSelfRefToJson);
        } else {
            responseDtoList = service.findAllByPortfolioId(portfolioId);
            responseDtoList.forEach(this::addSelfRefToJson);
        }
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<TransactionDtoResponse> save(@Valid @RequestBody TransactionDtoCreateRequest requestDto) {
        TransactionDtoResponse responseDto = service.save(requestDto);
        addSelfRefToJson(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<TransactionDtoResponse>> saveAll(
            @Valid @RequestBody ValidList<TransactionDtoCreateRequest> requestDtoList) {

        List<TransactionDtoResponse> responseDtoList = service.saveAll(requestDtoList);
        responseDtoList.forEach(this::addSelfRefToJson);
        return new ResponseEntity<>(responseDtoList, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransactionDtoResponse> update(@Valid @RequestBody TransactionDtoUpdateRequest requestDto) {
        TransactionDtoResponse responseDto = service.update(requestDto);
        addSelfRefToJson(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NumberFormat @PathVariable String id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void addSelfRefToJson(@Valid TransactionDtoResponse responseDto) {
        responseDto.add(linkTo(methodOn(TransactionController.class)
                .findById(responseDto.getId()))
                .withSelfRel());
    }

}
