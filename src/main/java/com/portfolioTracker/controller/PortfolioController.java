package com.portfolioTracker.controller;

import com.portfolioTracker.model.portfolio.dto.PortfolioRequestDto;
import com.portfolioTracker.model.portfolio.dto.PortfolioResponseDto;
import com.portfolioTracker.model.portfolio.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PortfolioResponseDto>> findAll() {
        List<PortfolioResponseDto> responseList = service.findAll();
        responseList.forEach(this::addLinkToResponseObject);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponseDto> findById(@NotNull @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PortfolioResponseDto> save(@NotNull @RequestBody PortfolioRequestDto requestDto) {
        PortfolioResponseDto responseDto = service.save(requestDto);
        addLinkToResponseObject(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping
    public ResponseEntity<PortfolioResponseDto> update(@NotNull @RequestBody PortfolioRequestDto requestDto) {
        PortfolioResponseDto responseDto = service.update(requestDto);
        addLinkToResponseObject(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NotNull @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private void addLinkToResponseObject(@NotNull PortfolioResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(PortfolioController.class).findById(responseDto.getId())).withSelfRel());
    }

}
