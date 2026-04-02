package ru.vadim.webfluxpatterns.sec03.util;

import ru.vadim.webfluxpatterns.sec03.dto.InventoryRequest;
import ru.vadim.webfluxpatterns.sec03.dto.OrchestrationRequestContext;
import ru.vadim.webfluxpatterns.sec03.dto.PaymentRequest;
import ru.vadim.webfluxpatterns.sec03.dto.ShippingRequest;

public class OrchestrationUtil {

    public static void buildRequestContext(OrchestrationRequestContext context) {
        buildInventoryRequest(context);
        buildPaymentRequest(context);
        buildShippingRequest(context);
    }

    private static void buildPaymentRequest(OrchestrationRequestContext context) {
        var paymentRequest = PaymentRequest.create(
                context.getOrderRequest().getUserId(),
                context.getProductPrice() * context.getOrderRequest().getQuantity(),
                context.getOrderId()
        );
        context.setPaymentRequest(paymentRequest);
    }

    private static void buildInventoryRequest(OrchestrationRequestContext context) {
        var inventoryRequest = InventoryRequest.create(
                context.getOrderId(),
                context.getOrderRequest().getProductId(),
                context.getOrderRequest().getQuantity()
        );
        context.setInventoryRequest(inventoryRequest);
    }

    private static void buildShippingRequest(OrchestrationRequestContext context) {
        var shippingRequest = ShippingRequest.create(
                context.getOrderRequest().getQuantity(),
                context.getOrderRequest().getUserId(),
                context.getOrderId()
        );
        context.setShippingRequest(shippingRequest);
    }
}
