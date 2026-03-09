package ru.vadim.webfluxpatterns.sec02.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.vadim.webfluxpatterns.sec02.client.DeltaClient;
import ru.vadim.webfluxpatterns.sec02.client.FrontierClient;
import ru.vadim.webfluxpatterns.sec02.client.JetBlueClient;
import ru.vadim.webfluxpatterns.sec02.dto.FlightResult;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FlightSearchService {

    private final DeltaClient deltaClient;
    private final FrontierClient frontierClient;
    private final JetBlueClient jetBlueClient;

    public Flux<FlightResult> getFlights(String from, String to) {
        return Flux.merge(
                        this.deltaClient.getFlights(from, to),
                        this.frontierClient.getFlights(from, to),
                        this.jetBlueClient.getFlights(from, to)
                )
                .take(Duration.ofSeconds(3));
    }
}