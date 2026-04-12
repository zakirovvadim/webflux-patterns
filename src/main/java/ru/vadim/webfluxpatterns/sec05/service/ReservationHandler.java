package ru.vadim.webfluxpatterns.sec05.service;

import reactor.core.publisher.Flux;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemRequest;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemResponse;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationType;

public abstract class ReservationHandler {

    protected abstract ReservationType getType();

    protected abstract Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux);
}
