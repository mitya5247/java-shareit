package ru.practicum.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class BookingDto {
    Long id;
    @Positive(message = "booker must not be negative or zero")
    Long bookerId;
    @Positive(message = "itemId must not be negative or zero")
    @NotNull(message = "itemId must not be null")
    Long itemId;
    State status = State.WAITING;
    LocalDateTime start;
    LocalDateTime end;
}
