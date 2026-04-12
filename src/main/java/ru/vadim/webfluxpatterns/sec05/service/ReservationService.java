package ru.vadim.webfluxpatterns.sec05.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemRequest;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemResponse;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationResponse;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationType;

@Service
public class ReservationService {

    private final Map<ReservationType, ReservationHandler> map;

    public ReservationService(List<ReservationHandler> handlers) {
        this.map = handlers.stream().collect(Collectors.toMap(
                ReservationHandler::getType,
                Function.identity()
        ));
    }

    public Mono<ReservationResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.groupBy(ReservationItemRequest::getType)
                .flatMap(this::aggregator)
                .collectList()
                .map(this::toResponse);
    }


    private Flux<ReservationItemResponse> aggregator(GroupedFlux<ReservationType, ReservationItemRequest> groupedFlux) {
        var key = groupedFlux.key();
        var handler = map.get(key);
        return handler.reserve(groupedFlux);
    }

    private ReservationResponse toResponse(List<ReservationItemResponse> items) {
        return ReservationResponse.create(
                UUID.randomUUID(),
                items.stream().mapToInt(ReservationItemResponse::getPrice).sum(),
                items
        );
    }
}
