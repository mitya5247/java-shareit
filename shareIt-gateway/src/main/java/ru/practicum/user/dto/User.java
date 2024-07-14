package ru.practicum.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    Long id;
    @Email
    @NotBlank(message = "email must not be empty and null")
    String email;
    @NotBlank(message = "name must not be empty and null")
    String name;
}