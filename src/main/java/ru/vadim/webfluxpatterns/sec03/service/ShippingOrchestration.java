package ru.vadim.webfluxpatterns.sec03.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec03.client.ShippingClient;
import ru.vadim.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec03.dto.Status;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
public class ShippingOrchestration extends Orchestrator {

    @Autowired
    private ShippingClient client;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext context) {
        return this.client.schedule(context.getShippingRequest())
                .doOnNext(context::setShippingResponse)
                .thenReturn(context);
    }

    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return context -> Status.SUCCESS.equals(context.getShippingResponse().getStatus());
    }

    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        return context -> Mono.just(context)
                .filter(isSuccess())
                .map(OrchestrationRequestContext::getShippingRequest)
                .flatMap(this.client::cancel)
                .subscribe();
    }
}
