package com.portfolioTracker.controller;

import com.portfolioTracker.model.transaction.dto.TransactionRequestDto;
import com.portfolioTracker.model.transaction.dto.TransactionResponseDto;
import com.portfolioTracker.model.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/transaction")
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
    public ResponseEntity<TransactionResponseDto> findById(@NotNull @PathVariable long id) {
        TransactionResponseDto responseDto = service.findById(id);
        addLink(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDto> save(@NotNull @RequestBody TransactionRequestDto request) {
        TransactionResponseDto response = service.save(request);
        addLink(response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TransactionResponseDto> update(@NotNull @RequestBody TransactionRequestDto request) {
        TransactionResponseDto response = service.update(request);
        addLink(response);
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@NotNull @PathVariable long id) {
        service.deleteById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

    private void addLink(@NotNull TransactionResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(TransactionController.class).findById(responseDto.getId())).withSelfRel());
    }

}
