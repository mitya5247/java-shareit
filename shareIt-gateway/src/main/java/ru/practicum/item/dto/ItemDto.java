package ru.practicum.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ItemDto {
    Long id;
    @NotBlank(message = "name must not be empty and null")
    String name;
    @NotBlank(message = "description must not be empty and null")
    String description;
    @NotNull(message = "available must not be null")
    Boolean available;
    BookingDto lastBooking;
    BookingDto nextBooking;
    Long requestId;
}
