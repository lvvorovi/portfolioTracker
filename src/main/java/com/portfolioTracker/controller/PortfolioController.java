package com.portfolioTracker.controller;

import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoCreateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoUpdateRequest;
import com.portfolioTracker.domain.portfolio.dto.PortfolioDtoResponse;
import com.portfolioTracker.domain.portfolio.service.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<PortfolioDtoResponse> findById(@NumberFormat @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDtoResponse>> findAll() {
        List<PortfolioDtoResponse> portfolioResponseList = service.findAll();
        portfolioResponseList.forEach(this::addLinkToResponseObject);
        portfolioResponseList.forEach(System.out::println);
        return ResponseEntity.ok(portfolioResponseList);
    }

    @PostMapping
    public ResponseEntity<PortfolioDtoResponse> save(@Valid @RequestBody PortfolioDtoCreateRequest requestDto) {
        PortfolioDtoResponse responseDto = service.save(requestDto);
        addLinkToResponseObject(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PortfolioDtoResponse> update(@Valid @RequestBody PortfolioDtoUpdateRequest requestDto) {
        PortfolioDtoResponse responseDto = service.update(requestDto);
        addLinkToResponseObject(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NumberFormat @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void addLinkToResponseObject(@Valid PortfolioDtoResponse responseDto) {
        responseDto.add(linkTo(methodOn(PortfolioController.class).findById(responseDto.getId())).withSelfRel());
    }

}
