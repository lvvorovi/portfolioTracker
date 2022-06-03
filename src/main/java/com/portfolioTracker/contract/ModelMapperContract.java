package com.portfolioTracker.contract;

import javax.validation.constraints.NotNull;

public interface ModelMapperContract<Entity, Request, Response> {

    Entity toEntity(@NotNull Request requestDto);

    Response toDto(@NotNull Entity entity);

}
