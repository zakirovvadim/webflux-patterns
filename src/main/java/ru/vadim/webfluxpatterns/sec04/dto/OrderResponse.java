package ru.vadim.webfluxpatterns.sec04.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class OrderResponse {

    private Integer userId;
    private Integer productId;
    private UUID orderId;
    private Status status;
    private Address shippingAddress;
    private String expectedDelivery;
}
