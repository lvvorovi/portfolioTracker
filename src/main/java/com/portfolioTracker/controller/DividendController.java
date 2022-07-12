package com.portfolioTracker.controller;

import com.portfolioTracker.core.ValidList;
import com.portfolioTracker.domain.dividend.dto.DividendDtoCreateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoUpdateRequest;
import com.portfolioTracker.domain.dividend.dto.DividendDtoResponse;
import com.portfolioTracker.domain.dividend.service.DividendService;
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
@RequestMapping("/api/v1/dividends")
public class DividendController {

    private final DividendService service;

    @GetMapping("/{id}")
    public ResponseEntity<DividendDtoResponse> findById(@NumberFormat @PathVariable Long id) {
        DividendDtoResponse responseDto = service.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<DividendDtoResponse>> findALl() {
        List<DividendDtoResponse> responseDtoList = service.findAll();
        responseDtoList.forEach(this::addSelfRefToJson);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<DividendDtoResponse> save(@Valid @RequestBody DividendDtoCreateRequest requestDto) {
        DividendDtoResponse responseDto = service.save(requestDto);
        addSelfRefToJson(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<DividendDtoResponse>> saveAll(
            @RequestBody @Valid ValidList<DividendDtoCreateRequest> requestDtoList) {
        List<DividendDtoResponse> responseDtoList = service.saveAll(requestDtoList);
        responseDtoList.forEach(this::addSelfRefToJson);
        return new ResponseEntity<>(responseDtoList, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NumberFormat @PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        service.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<DividendDtoResponse> update(@Valid @RequestBody DividendDtoUpdateRequest requestDto) {
        DividendDtoResponse responseDto = service.update(requestDto);
        addSelfRefToJson(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    private void addSelfRefToJson(@Valid DividendDtoResponse responseDto) {
        responseDto.add(linkTo(methodOn(DividendController.class).findById(responseDto.getId())).withSelfRel());
    }
}
