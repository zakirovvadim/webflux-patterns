package ru.vadim.webfluxpatterns.sec05.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "create")
public class ReservationItemResponse {
    UUID itemId;
    ReservationType type;
    String category;
    String city;
    LocalDate from;
    LocalDate to;
    Integer price;
}
