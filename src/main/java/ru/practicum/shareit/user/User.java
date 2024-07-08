package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

import static javax.persistence.GenerationType.*;

/**
 * TODO Sprint add-controllers.
 */
@Valid
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Positive(message = "id must be positive")
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    @Column(name = "email", unique = true)
    @Email
    @NotBlank(message = "email must not be empty")
    @NotNull(message = "email must not be null")
    String email;
    @Column(name = "name")
    @NotEmpty(message = "name must not be null or empty")
    String name;

}
