package ru.vadim.webfluxpatterns.sec05.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationItemRequest {
    ReservationType type;
    String category;
    String city;
    LocalDate from;
    LocalDate to;
}
