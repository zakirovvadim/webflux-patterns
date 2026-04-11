package ru.vadim.webfluxpatterns.sec04.service;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import ru.vadim.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec04.exception.OrderFulfilmentFailure;

public abstract class Orchestrator {

    public abstract Mono<OrchestrationRequestContext> create(OrchestrationRequestContext context);
    public abstract Predicate<OrchestrationRequestContext> isSuccess();
    public abstract Consumer<OrchestrationRequestContext> cancel();

    protected BiConsumer<OrchestrationRequestContext, SynchronousSink<OrchestrationRequestContext>> statusHandle() {
        return (context, sink) -> {
            if (isSuccess().test(context)) {
                sink.next(context);
            } else {
                sink.error(new OrderFulfilmentFailure());
            }
        };
    }
}
