package com.portfolioTracker.core.contract;

public interface DomainMapper<E, C, U, R> {

    E updateToEntity(U dto);

    E createToEntity(C dto);

    R toDto(E entity);
}
