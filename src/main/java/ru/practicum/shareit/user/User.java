package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class User {
    @Positive(message = "id must be positive")
    Long id;
    @Email
    @NotNull(message = "email must not be null")
    String email;
    String name;
}
