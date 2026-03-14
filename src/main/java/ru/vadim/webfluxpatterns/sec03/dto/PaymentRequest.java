package ru.vadim.webfluxpatterns.sec03.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class PaymentRequest {
    private Integer userId;
    private Integer amount;
    private UUID orderId;
}
