package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    @NotNull(message = "email must not be null")
    String email;
    @Column(name = "name")
    String name;

}
