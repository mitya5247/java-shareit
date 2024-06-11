package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemDto {
    @Positive
    Long id;
    @NotEmpty(message = "name must not be empty")
    String name;
    @NotEmpty(message = "description must not be empty")
    String description;
    @NotNull(message = "available must not be null")
    Boolean available;
}
