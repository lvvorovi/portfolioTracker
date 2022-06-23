package com.portfolioTracker.model.dto.errorDto;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Data
public class ErrorDto {

    private String message;

    public ErrorDto(@NotEmpty String message) {
        this.message = message;
    }

}
