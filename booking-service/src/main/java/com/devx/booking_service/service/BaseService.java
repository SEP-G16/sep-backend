package com.devx.booking_service.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

public abstract class BaseService<T> {
    protected Scheduler jdbcScheduler;

    protected BaseService(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler)
    {
        this.jdbcScheduler = jdbcScheduler;
    }

    public abstract ResponseEntity<Mono<T>> insert(T t);
    public abstract ResponseEntity<Flux<T>> getAll();
}
