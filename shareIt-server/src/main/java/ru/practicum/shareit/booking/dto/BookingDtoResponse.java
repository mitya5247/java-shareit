package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoResponse {
    Long id;
    User booker;
    ItemDto item;
    State status;
    LocalDateTime start;
    LocalDateTime end;
}
