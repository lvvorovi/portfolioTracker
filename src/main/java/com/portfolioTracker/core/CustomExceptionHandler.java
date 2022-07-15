package com.portfolioTracker.core;

import com.portfolioTracker.core.validation.ValidationException;
import com.portfolioTracker.domain.dto.errorDto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
            log.debug("Request with ID {}, errors: {}", MDC.get("request_id"), message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(message));
        }

        if (ex instanceof ValidationException) {
            log.debug("Request with ID {} caught ValidationException of type {}: {}",
                    MDC.get("request_id"), ex.getClass(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
        }

        if (ex instanceof AccessDeniedException) {
            log.debug("Request with ID {} caught AccessDeniedException of type {}: {}",
                    MDC.get("request_id"), ex.getClass(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(ex.getMessage()));
        }

        log.error(ex.toString());
        ex.printStackTrace();
        return ResponseEntity.internalServerError().body(new ErrorDto(ex.toString()));
    }

}
