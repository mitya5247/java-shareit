package ru.practicum.shareit.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.State;

import javax.persistence.*;
import javax.validation.constraints.Positive;
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
    @Positive(message = "booker must not be negative or zero")
    Long bookerId;
    @Positive(message = "itemId must not be negative or zero")
    Long itemId;
    @Enumerated(EnumType.STRING)
    State status = State.WAITING;
    LocalDateTime start;
    LocalDateTime end;
}
