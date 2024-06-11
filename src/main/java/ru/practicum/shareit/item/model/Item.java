package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "owner")
    Long owner;
    @Column(name = "available")
    boolean available;
}
