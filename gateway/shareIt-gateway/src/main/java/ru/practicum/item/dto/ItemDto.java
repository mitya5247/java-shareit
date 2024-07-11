package ru.practicum.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ItemDto {
    @NotBlank(message = "name must not be empty")
    @NotNull(message = "name must not be null")
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
