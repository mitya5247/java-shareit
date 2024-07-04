package ru.practicum.shareit.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.State;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "booker must not be null")
    Long bookerId;
    @NotNull(message = "itemId must not be null")
    Long itemId;
    @Enumerated(EnumType.STRING)
    State status = State.WAITING;
    LocalDateTime start;
    LocalDateTime end;
}
