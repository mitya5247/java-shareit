package ru.practicum.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-item-requests.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    Long id;
    String name;
    String description;
    Long requestId;
    Boolean available;
}
