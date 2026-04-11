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
public class PaymentResponse {

    private UUID paymentId;
    private Integer userId;
    private String name;
    private Integer balance;
    private Status status;
}
