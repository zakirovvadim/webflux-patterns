package ru.vadim.webfluxpatterns.sec05.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@ToString
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor(staticName = "create")
public class CarReservationResponse {

    UUID reservationId;
    String city;
    LocalDate pickup;
    LocalDate drop;
    String category;
    Integer price;
}
