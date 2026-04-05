package ru.vadim.webfluxpatterns.sec03.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec03.dto.Status;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderFulfilmentService {

    @Autowired
    private List<Orchestrator> orchestrators;

    public Mono<OrchestrationRequestContext> placeOrder(OrchestrationRequestContext context) {
        List<Mono<OrchestrationRequestContext>> list = orchestrators.stream()
                .map(o -> o.create(context))
                .collect(Collectors.toList());

        return Mono.zip(list, a -> a[0])
                .cast(OrchestrationRequestContext.class)
                .doOnNext(this::updateStatus);
    }

    private void updateStatus(OrchestrationRequestContext context) {
        boolean allSuccess = this.orchestrators.stream().allMatch(o -> o.isSuccess().test(context));
        Status status = allSuccess ? Status.SUCCESS : Status.FAILED;
        context.setStatus(status);
    }
}
