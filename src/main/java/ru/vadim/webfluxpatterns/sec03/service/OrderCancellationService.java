package ru.vadim.webfluxpatterns.sec03.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import ru.vadim.webfluxpatterns.sec03.dto.OrchestrationRequestContext;

import java.util.List;

@Service
public class OrderCancellationService {

    private Sinks.Many<OrchestrationRequestContext> sink;
    private Flux<OrchestrationRequestContext> flux;

    @Autowired
    private List<Orchestrator> orchestrators;

    @PostConstruct
    public void init() {
        sink = Sinks.many().multicast().onBackpressureBuffer();
        this.flux = this.sink.asFlux();
        orchestrators.forEach(o -> this.flux.subscribe(o.cancel()));
    }

    public void cancelOrder(OrchestrationRequestContext context) {
        this.sink.tryEmitNext(context);
    }
}
