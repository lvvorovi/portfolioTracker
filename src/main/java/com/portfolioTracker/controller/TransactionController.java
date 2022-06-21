package com.portfolioTracker.controller;

import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.model.transaction.service.TransactionService;
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
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> findAll() {
        List<TransactionResponseDto> transactionList = service.findAll();
        transactionList.forEach(this::addLink);
        return ResponseEntity.ok(transactionList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> findById(@NumberFormat @PathVariable Long id) {
        TransactionResponseDto responseDto = service.findById(id);
        addLink(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> save(@Valid @RequestBody TransactionRequestDto requestDto) {
        TransactionResponseDto responseDto = service.save(requestDto);
        addLink(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<TransactionResponseDto>> saveAll(@Valid @RequestBody List<TransactionRequestDto> requestDtoList) {
        List<TransactionResponseDto> responseDtoList = service.saveAll(requestDtoList);
        responseDtoList.forEach(this::addLink);
        return new ResponseEntity<>(responseDtoList, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransactionResponseDto> update(@Valid @RequestBody TransactionRequestDto requestDto) {
        TransactionResponseDto responseDto = service.update(requestDto);
        addLink(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NumberFormat @PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAll() {
        service.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private void addLink(@Valid TransactionResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(TransactionController.class).findById(responseDto.getId())).withSelfRel());
    }

}
