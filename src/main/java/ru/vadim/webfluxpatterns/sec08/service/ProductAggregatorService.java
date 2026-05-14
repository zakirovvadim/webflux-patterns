package ru.vadim.webfluxpatterns.sec08.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec08.client.ProductClient;
import ru.vadim.webfluxpatterns.sec08.client.ReviewClient;
import ru.vadim.webfluxpatterns.sec08.dto.Product;
import ru.vadim.webfluxpatterns.sec08.dto.ProductAggregate;
import ru.vadim.webfluxpatterns.sec08.dto.Review;

@Service
@RequiredArgsConstructor
public class ProductAggregatorService {

    private final ProductClient productClient;
    private final ReviewClient reviewClient;

    public Mono<ProductAggregate> aggregate(Integer id) {
        return Mono.zip(
                        productClient.getProduct(id),
                        reviewClient.getReviews(id)
                )
                .map(tuple -> toDto(tuple.getT1(), tuple.getT2()));
    }

    private ProductAggregate toDto(Product product, List<Review> rewiews) {
        return ProductAggregate.create(
                product.getId(),
                product.getCategory(),
                product.getDescription(),
                rewiews
        );
    }
}
