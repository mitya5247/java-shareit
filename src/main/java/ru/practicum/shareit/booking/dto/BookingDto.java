package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
public class BookingDto {
    Long id;
    Long booker;
    Long itemId;
    State status = State.WAITING;
    @FutureOrPresent(message = "start must be not in future")
    Timestamp start;
    @FutureOrPresent(message = "end must be in future")
    Timestamp end;
}
