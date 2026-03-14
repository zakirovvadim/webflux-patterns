package ru.vadim.webfluxpatterns.sec03.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor(staticName = "create")
public class OrderRequest {

    private Integer userId;
    private Integer productId;
    private Integer quantity;
}
