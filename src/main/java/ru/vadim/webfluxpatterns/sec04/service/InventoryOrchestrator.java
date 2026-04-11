package ru.vadim.webfluxpatterns.sec04.service;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec04.client.InventoryClient;
import ru.vadim.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec04.dto.Status;

@Service
public class InventoryOrchestrator extends Orchestrator {

    @Autowired
    private InventoryClient inventoryClient;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext context) {
        return this.inventoryClient.deduct(context.getInventoryRequest())
                .doOnNext(context::setInventoryResponse)
                .thenReturn(context)
                .handle(this.statusHandle());
    }

    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return ctx -> Objects.nonNull(ctx.getInventoryResponse()) && Status.SUCCESS.equals(ctx.getInventoryResponse().getStatus());
    }

    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        return context ->  Mono.just(context)
                .filter(isSuccess())
                .map(OrchestrationRequestContext::getInventoryRequest)
                .flatMap(this.inventoryClient::restore)
                .subscribe();
    }
}
