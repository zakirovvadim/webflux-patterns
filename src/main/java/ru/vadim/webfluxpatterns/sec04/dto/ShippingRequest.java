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
public class ShippingRequest {

    private Integer quantity;
    private Integer userId;
    private UUID inventoryId;

}
