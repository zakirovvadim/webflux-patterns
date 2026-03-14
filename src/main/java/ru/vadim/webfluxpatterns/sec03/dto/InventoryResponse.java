package ru.vadim.webfluxpatterns.sec03.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class InventoryResponse {

    private Integer productId;
    private Integer quantity;
    private Integer remainingQuantity;
    private Status status;
}
