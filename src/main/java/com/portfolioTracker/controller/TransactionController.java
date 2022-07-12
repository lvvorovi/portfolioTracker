package com.portfolioTracker.controller;

import com.portfolioTracker.core.ValidList;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoCreateRequest;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoResponse;
import com.portfolioTracker.domain.transaction.dto.TransactionDtoUpdateRequest;
import com.portfolioTracker.domain.transaction.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<TransactionDtoResponse>> findAll(@RequestParam(required = false) Long portfolioId) {
        List<TransactionDtoResponse> transactionList = service.findAll(portfolioId);
        transactionList.forEach(this::addLink);
        return ResponseEntity.ok(transactionList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDtoResponse> findById(@NumberFormat @PathVariable Long id) {
        TransactionDtoResponse responseDto = service.findById(id);
        addLink(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<TransactionDtoResponse> save(@Valid @RequestBody TransactionDtoCreateRequest requestDto) {
        TransactionDtoResponse responseDto = service.save(requestDto);
        addLink(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<TransactionDtoResponse>> saveAll(@Valid @RequestBody ValidList<TransactionDtoCreateRequest> requestDtoList) {
        List<TransactionDtoResponse> responseDtoList = service.saveAll(requestDtoList);
        responseDtoList.forEach(this::addLink);
        return new ResponseEntity<>(responseDtoList, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransactionDtoResponse> update(@Valid @RequestBody TransactionDtoUpdateRequest requestDto) {
        TransactionDtoResponse responseDto = service.update(requestDto);
        addLink(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NumberFormat @PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void addLink(@Valid TransactionDtoResponse responseDto) {
        responseDto.add(linkTo(methodOn(TransactionController.class).findById(responseDto.getId())).withSelfRel());
    }

}
