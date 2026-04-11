package ru.vadim.webfluxpatterns.sec04.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.vadim.webfluxpatterns.sec04.client.ProductClient;
import ru.vadim.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec04.dto.Product;
import ru.vadim.webfluxpatterns.sec04.dto.Status;
import ru.vadim.webfluxpatterns.sec04.util.OrchestrationUtil;

@Service
public class OrderFulfilmentService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private PaymentOrchestration paymentOrchestration;

    @Autowired
    private InventoryOrchestrator inventoryOrchestrator;

    @Autowired
    private ShippingOrchestrator shippingOrchestration;

    public Mono<OrchestrationRequestContext> placeOrder(OrchestrationRequestContext context) {
        return this.getProduct(context)
                .doOnNext(OrchestrationUtil::buildPaymentRequest)
                .flatMap(this.paymentOrchestration::create)
                .doOnNext(OrchestrationUtil::buildInventoryRequest)
                .flatMap(this.inventoryOrchestrator::create)
                .doOnNext(OrchestrationUtil::buildShippingRequest)
                .flatMap(this.shippingOrchestration::create)
                .doOnNext(c -> c.setStatus(Status.SUCCESS))
                .doOnError(ex -> context.setStatus(Status.FAILED))
                .onErrorReturn(context);
    }

    private Mono<OrchestrationRequestContext> getProduct(OrchestrationRequestContext context) {
        return this.productClient.getProduct(context.getOrderRequest().getProductId())
                .map(Product::getPrice)
                .doOnNext(context::setProductPrice)
                .map(i -> context);
    }
}
