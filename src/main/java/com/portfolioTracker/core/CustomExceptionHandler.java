package com.portfolioTracker.core;

import com.portfolioTracker.core.contract.ValidationException;
import com.portfolioTracker.model.dto.errorDto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handle(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            String message = ((MethodArgumentNotValidException) ex).getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            log.info("Request with ID {}, errors: {}", MDC.get("request_id"), message);
            return ResponseEntity.badRequest().body(new ErrorDto(message));
        }

        if (ex instanceof ValidationException) {
            log.info("Request with ID {} caught ValidationException of type {}: {}",
                    MDC.get("request_id"), ex.getClass(), ex.getMessage());
            return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
        }

        log.error(ex.toString());
        ex.printStackTrace();
        return ResponseEntity.internalServerError().body(new ErrorDto(ex.toString()));
    }

}
