package ru.practicum.shareit.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.State;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class BookingDto {
    Long id;
    Long bookerId;
    Long itemId;
    State status = State.WAITING;
    LocalDateTime start;
    LocalDateTime end;
}
