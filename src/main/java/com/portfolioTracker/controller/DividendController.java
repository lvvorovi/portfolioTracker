package com.portfolioTracker.controller;

import com.portfolioTracker.model.dividend.dto.DividendRequestDto;
import com.portfolioTracker.model.dividend.dto.DividendResponseDto;
import com.portfolioTracker.model.dividend.service.DividendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/dividend")
public class DividendController {

    private final DividendService service;

    public DividendController(DividendService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DividendResponseDto> save(@NotNull @RequestBody DividendRequestDto requestDto) {
        DividendResponseDto responseDto = service.save(requestDto);
        addSelfRefToJson(responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/all")
    public ResponseEntity<List<DividendResponseDto>> saveAll(
            @NotNull @RequestBody List<DividendRequestDto> requestDtos) {
        List<DividendResponseDto> responseDtos = service.saveAll(requestDtos);
        responseDtos.forEach(this::addSelfRefToJson);
        return new ResponseEntity<>(responseDtos, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DividendResponseDto> findById(@NotNull @RequestParam Long id) {
        DividendResponseDto responseDto = service.findById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<DividendResponseDto>> findALl() {
        List<DividendResponseDto> responseDtos = service.findAll();
        responseDtos.forEach(this::addSelfRefToJson);
        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@NotNull @RequestParam Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        service.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<DividendResponseDto> updateById(@NotNull @RequestBody DividendRequestDto requestDto) {
        DividendResponseDto responseDto = service.update(requestDto);
        addSelfRefToJson(responseDto);
        return ResponseEntity.ok(responseDto);
    }

    private void addSelfRefToJson(@NotNull DividendResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(DividendController.class, findById(responseDto.getId()))).withSelfRel());
    }
}
