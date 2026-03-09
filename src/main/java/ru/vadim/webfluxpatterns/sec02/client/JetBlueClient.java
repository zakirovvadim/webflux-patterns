package ru.vadim.webfluxpatterns.sec02.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec02.dto.FlightResult;

@Service
public class JetBlueClient {

    private static final String JETBLUE = "JETBLUE";
    private final WebClient client;

    public JetBlueClient(@Value("${sec02.jetblue.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<FlightResult> getFlights(String from, String to) {
        return this.client
                .get()
                .uri("{from}/{to}", from, to)
                .retrieve()
                .bodyToFlux(FlightResult.class)
                .doOnNext(r -> this.normalizeResponse(r, from, to))
                .onErrorResume(ex -> Mono.empty());
    }

    private void normalizeResponse(FlightResult result, String from, String to) {
        result.setFrom(from);
        result.setTo(to);
        result.setAirline(JETBLUE);
    }
}
