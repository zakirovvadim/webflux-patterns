package ru.vadim.webfluxpatterns.sec04.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec04.client.ProductClient;
import ru.vadim.webfluxpatterns.sec04.dto.Address;
import ru.vadim.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec04.dto.OrderRequest;
import ru.vadim.webfluxpatterns.sec04.dto.OrderResponse;
import ru.vadim.webfluxpatterns.sec04.dto.Product;
import ru.vadim.webfluxpatterns.sec04.dto.Status;
import ru.vadim.webfluxpatterns.sec04.util.DebugUtil;
import ru.vadim.webfluxpatterns.sec04.util.OrchestrationUtil;

@Service
public class OrchestratorService {

    @Autowired
    private OrderFulfilmentService fulfilmentService;

    @Autowired
    private OrderCancellationService cancellationService;

    public Mono<OrderResponse> placeOrder(Mono<OrderRequest> mono) {
        return mono
                .map(OrchestrationRequestContext::new)
                .flatMap(fulfilmentService::placeOrder)
                .doOnNext(this::doOrderProcessing)
                .doOnNext(DebugUtil::print) // just for debug
                .map(this::toOrderResponse);
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
