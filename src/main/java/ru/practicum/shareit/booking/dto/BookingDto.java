package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
public class BookingDto {
    Long id;
    @NotNull(message = "booker must not be null")
    Long booker;
    @NotNull(message = "itemId must not be null")
    Long itemId;
    @Enumerated(EnumType.STRING)
    State status = State.WAITING;
    LocalDateTime start;
    LocalDateTime end;
}
