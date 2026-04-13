package ru.vadim.webfluxpatterns.sec06.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec06.dto.Review;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewClient {
    private final WebClient client;

    public ReviewClient(@Value("${sec06.review.service}") String baseUrl) {
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<List<Review>> getReviews(Integer id) {
        return this.client
                .get()
                .uri("{id}", id)
                .retrieve()
                .bodyToFlux(Review.class)
                .timeout(Duration.ofMillis(500))
                .collectList()
                .onErrorReturn(Collections.emptyList());
    }
}
