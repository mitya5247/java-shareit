package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ItemDto {
    @Positive
    Long id;
    @NotEmpty(message = "name must not be empty")
    String name;
    @NotBlank(message = "description must not be empty")
    @NotNull(message = "description must not be null")
    String description;
    @NotNull(message = "available must not be null")
    Boolean available;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDto> comments;
    Long requestId;
}
