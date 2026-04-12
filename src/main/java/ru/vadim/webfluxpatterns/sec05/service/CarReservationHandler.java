package ru.vadim.webfluxpatterns.sec05.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.vadim.webfluxpatterns.sec05.client.CarClient;
import ru.vadim.webfluxpatterns.sec05.dto.CarReservationRequest;
import ru.vadim.webfluxpatterns.sec05.dto.CarReservationResponse;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemRequest;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemResponse;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationType;

import static ru.vadim.webfluxpatterns.sec05.dto.ReservationType.CAR;

@Service
public class CarReservationHandler extends ReservationHandler {

    @Autowired
    protected CarClient carClient;

    @Override
    protected ReservationType getType() {
        return CAR;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(this::toCarRequest)
                .transform(this.carClient::reserve)
                .map(this::toResponse);
    }

    private CarReservationRequest toCarRequest(ReservationItemRequest request) {
        return CarReservationRequest.create(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }

    private ReservationItemResponse toResponse(CarReservationResponse response) {
        return ReservationItemResponse.create(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getPickup(),
                response.getDrop(),
                response.getPrice()
        );
    }
}
