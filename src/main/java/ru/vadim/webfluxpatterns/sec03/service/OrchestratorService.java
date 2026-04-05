package ru.vadim.webfluxpatterns.sec03.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec01.client.ProductClient;
import ru.vadim.webfluxpatterns.sec03.dto.*;
import ru.vadim.webfluxpatterns.sec03.util.OrchestrationUtil;

@Service
public class OrchestratorService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderFulfilmentService fulfilmentService;

    @Autowired
    private OrderCancellationService cancellationService;

    public Mono<OrderResponse> placeOrder(Mono<OrderRequest> mono) {
       return  mono
                .map(OrchestrationRequestContext::new)
               .flatMap(this::getProduct)
               .doOnNext(OrchestrationUtil::buildRequestContext)
               .flatMap(fulfilmentService::placeOrder)
               .doOnNext(this::doOrderProcessing)
               .map(this::toOrderResponse);
    }

    private Mono<OrchestrationRequestContext> getProduct(OrchestrationRequestContext context) {
        return this.productClient.getProduct(context.getOrderRequest().getProductId())
                .map(product -> product.getPrice())
                .doOnNext(context::setProductPrice)
                .thenReturn(context);
    }

    private void doOrderProcessing(OrchestrationRequestContext context) {
        if (Status.FAILED.equals(context.getStatus())) {
            this.cancellationService.cancelOrder(context);
        }
    }

    private OrderResponse toOrderResponse(OrchestrationRequestContext context) {
        boolean isSuccess = Status.SUCCESS.equals(context.getStatus());
        Address address = isSuccess ? context.getShippingResponse().getAddress() : null;
        String deliveryDate = isSuccess ? context.getShippingResponse().getExpectedDelivery() : null;

        return OrderResponse.create(
                context.getOrderRequest().getUserId(),
                context.getOrderRequest().getProductId(),
                context.getOrderId(),
                context.getStatus(),
                address,
                deliveryDate
        );
    }
}
