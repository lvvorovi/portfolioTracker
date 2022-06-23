package com.portfolioTracker.controller;

import com.portfolioTracker.core.ValidList;
import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dividend.service.DividendService;
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
@RequestMapping("/api/v1/dividends")
public class DividendController {

    private final DividendService service;

    public DividendController(DividendService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DividendResponseDto> findById(@NumberFormat @PathVariable Long id) {
        DividendResponseDto responseDto = service.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<DividendResponseDto>> findALl() {
        List<DividendResponseDto> responseDtoList = service.findAll();
        responseDtoList.forEach(this::addSelfRefToJson);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping
    public ResponseEntity<DividendResponseDto> save(@Valid @RequestBody DividendRequestDto requestDto) {
        DividendResponseDto responseDto = service.save(requestDto);
        addSelfRefToJson(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<DividendResponseDto>> saveAll(
            @RequestBody @Valid ValidList<DividendRequestDto> requestDtoList) {
        List<DividendResponseDto> responseDtoList = service.saveAll(requestDtoList);
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
    public ResponseEntity<DividendResponseDto> update(@Valid @RequestBody DividendRequestDto requestDto) {
        DividendResponseDto responseDto = service.update(requestDto);
        addSelfRefToJson(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    private void addSelfRefToJson(@Valid DividendResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(DividendController.class).findById(responseDto.getId())).withSelfRel());
    }
}
