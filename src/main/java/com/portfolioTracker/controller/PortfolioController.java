package com.portfolioTracker.controller;

import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.service.PortfolioService;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponseDto>> findAll() {
        List<PortfolioResponseDto> portfolioResponseList = service.findAll();
        portfolioResponseList.forEach(this::addLinkToResponseObject);
        return ResponseEntity.ok(portfolioResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> findById(@NumberFormat @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PortfolioResponseDto> save(@Valid @RequestBody PortfolioRequestDto requestDto) {
        PortfolioResponseDto responseDto = service.save(requestDto);
        addLinkToResponseObject(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<PortfolioResponseDto> update(@Valid @RequestBody PortfolioRequestDto requestDto) {
        PortfolioResponseDto responseDto = service.update(requestDto);
        addLinkToResponseObject(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NumberFormat @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private void addLinkToResponseObject(@Valid PortfolioResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(PortfolioController.class).findById(responseDto.getId())).withSelfRel());
    }

}
