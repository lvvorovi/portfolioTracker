package com.portfolioTracker.core;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Getter
@Setter
@NoArgsConstructor
public class ErrorDto {

    private String message;

    public ErrorDto(@NotEmpty String message) {
        this.message = message;
    }

}
