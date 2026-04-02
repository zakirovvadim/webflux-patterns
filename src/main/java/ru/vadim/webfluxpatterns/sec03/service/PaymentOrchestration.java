package ru.vadim.webfluxpatterns.sec03.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec03.client.UserClient;
import ru.vadim.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec03.dto.Status;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Component
public class PaymentOrchestration extends Orchestrator {

    @Autowired
    private UserClient userClient;

    @Override
    public Mono<OrchestrationRequestContext> create(OrchestrationRequestContext context) {
        return userClient.deduct(context.getPaymentRequest())
                .doOnNext(context::setPaymentResponse)
                .dematerialize();
    }

    @Override
    public Predicate<OrchestrationRequestContext> isSuccess() {
        return context -> Status.SUCCESS.equals(context.getPaymentResponse().getStatus());
    }

    @Override
    public Consumer<OrchestrationRequestContext> cancel() {
        return context -> Mono.just(context)
                .filter(isSuccess())
                .map(OrchestrationRequestContext::getPaymentRequest)
                .flatMap(this.userClient::refund)
                .subscribe();
    }
}
