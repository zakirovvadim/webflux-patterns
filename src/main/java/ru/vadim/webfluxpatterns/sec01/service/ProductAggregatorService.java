package ru.vadim.webfluxpatterns.sec01.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec01.client.ProductClient;
import ru.vadim.webfluxpatterns.sec01.client.PromotionClient;
import ru.vadim.webfluxpatterns.sec01.client.ReviewClient;
import ru.vadim.webfluxpatterns.sec01.dto.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAggregatorService {

    private final ProductClient productClient;
    private final PromotionClient promotionClient;
    private final ReviewClient reviewClient;

    public Mono<ProductAggregate> aggregate(Integer id) {
        return Mono.zip(
                        productClient.getProduct(id),
                        promotionClient.getPromotion(id),
                        reviewClient.getReviews(id)
                )
                .map(tuple -> toDto(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    private ProductAggregate toDto(ProductResponse product, PromotionResponse promotion, List<Review> rewiews) {
        var price = new Price();
        var amountSaved = product.getPrice() * promotion.getDiscount() / 100;
        var discounterPrice = product.getPrice() - amountSaved;
        price.setListPrice(product.getPrice());
        price.setAmountSaved(amountSaved);
        price.setDiscount(promotion.getDiscount());
        price.setDiscountedPrice(discounterPrice);
        price.setEndDate(promotion.getEndDate());
        return ProductAggregate.create(
                product.getId(),
                product.getCategory(),
                product.getDescription(),
                price,
                rewiews
        );
    }
}
