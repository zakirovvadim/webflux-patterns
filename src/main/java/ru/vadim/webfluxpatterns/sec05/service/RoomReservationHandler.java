package ru.vadim.webfluxpatterns.sec05.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.vadim.webfluxpatterns.sec05.client.RoomClient;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemRequest;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationItemResponse;
import ru.vadim.webfluxpatterns.sec05.dto.ReservationType;
import ru.vadim.webfluxpatterns.sec05.dto.RoomReservationRequest;
import ru.vadim.webfluxpatterns.sec05.dto.RoomReservationResponse;

import static ru.vadim.webfluxpatterns.sec05.dto.ReservationType.ROOM;

@Service
public class RoomReservationHandler extends ReservationHandler {

    @Autowired
    protected RoomClient carClient;

    @Override
    protected ReservationType getType() {
        return ROOM;
    }

    @Override
    protected Flux<ReservationItemResponse> reserve(Flux<ReservationItemRequest> flux) {
        return flux.map(this::toRoomRequest)
                .transform(this.carClient::reserve)
                .map(this::toResponse);
    }

    private RoomReservationRequest toRoomRequest(ReservationItemRequest request) {
        return RoomReservationRequest.create(
                request.getCity(),
                request.getFrom(),
                request.getTo(),
                request.getCategory()
        );
    }

    private ReservationItemResponse toResponse(RoomReservationResponse response) {
        return ReservationItemResponse.create(
                response.getReservationId(),
                this.getType(),
                response.getCategory(),
                response.getCity(),
                response.getCheckIn(),
                response.getCheckOut(),
                response.getPrice()
        );
    }
}
