package com.portfolioTracker.contract;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
public interface ModelMapperContract<Entity, Request, Response> {

    Entity toEntity(@NotNull Request requestDto);

    Response toDto(@NotNull Entity entity);

}
