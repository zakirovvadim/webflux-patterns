package ru.vadim.webfluxpatterns.sec04.util;

import ru.vadim.webfluxpatterns.sec04.dto.InventoryRequest;
import ru.vadim.webfluxpatterns.sec04.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec04.dto.PaymentRequest;
import ru.vadim.webfluxpatterns.sec04.dto.ShippingRequest;

public class OrchestrationUtil {

    public static void buildPaymentRequest(OrchestrationRequestContext context) {
        var paymentRequest = PaymentRequest.create(
                context.getOrderRequest().getUserId(),
                context.getProductPrice() * context.getOrderRequest().getQuantity(),
                context.getOrderId()
        );
        context.setPaymentRequest(paymentRequest);
    }

    public static void buildInventoryRequest(OrchestrationRequestContext context) {
        var inventoryRequest = InventoryRequest.create(
                context.getPaymentResponse().getPaymentId(),
                context.getOrderRequest().getProductId(),
                context.getOrderRequest().getQuantity()
        );
        context.setInventoryRequest(inventoryRequest);
    }

    public static void buildShippingRequest(OrchestrationRequestContext context) {
        var shippingRequest = ShippingRequest.create(
                context.getOrderRequest().getQuantity(),
                context.getOrderRequest().getUserId(),
                context.getInventoryResponse().getInventoryId()
        );
        context.setShippingRequest(shippingRequest);
    }
}
