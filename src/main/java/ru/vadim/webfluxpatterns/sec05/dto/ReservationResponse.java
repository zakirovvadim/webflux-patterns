package ru.vadim.webfluxpatterns.sec05.dto;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationResponse {
    UUID reservationId;
    Integer price;
    List<ReservationItemResponse> items;
}
