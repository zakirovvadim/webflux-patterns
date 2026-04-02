package ru.vadim.webfluxpatterns.sec03.service;

import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec03.dto.OrchestrationRequestContext;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Orchestrator {

    public abstract Mono<OrchestrationRequestContext> create(OrchestrationRequestContext context);
    public abstract Predicate<OrchestrationRequestContext> isSuccess();
    public abstract Consumer<OrchestrationRequestContext> cancel();
}
