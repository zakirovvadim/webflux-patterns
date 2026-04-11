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
public class InventoryResponse {

    private UUID inventoryId;
    private Integer productId;
    private Integer quantity;
    private Integer remainingQuantity;
    private Status status;
}
