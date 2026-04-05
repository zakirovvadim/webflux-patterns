package ru.vadim.webfluxpatterns.sec03.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec03.dto.OrderRequest;
import ru.vadim.webfluxpatterns.sec03.dto.OrderResponse;
import ru.vadim.webfluxpatterns.sec03.service.OrchestratorService;

@RestController
@RequestMapping("sec03")
public class OrderController {

    @Autowired
    private OrchestratorService service;

    @PostMapping("order")
    public Mono<ResponseEntity<OrderResponse>> placeOrder(@RequestBody Mono<OrderRequest> mono) {
        return this.service.placeOrder(mono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
