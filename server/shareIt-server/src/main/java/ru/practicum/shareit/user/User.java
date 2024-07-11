package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.*;

import static javax.persistence.GenerationType.*;

/**
 * TODO Sprint add-controllers.
 */
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
    String email;
    @Column(name = "name")
    String name;
}
