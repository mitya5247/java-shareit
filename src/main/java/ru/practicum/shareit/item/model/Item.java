package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Mapper;

import javax.persistence.*;
import java.util.List;

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
    @OneToOne
    @JoinColumn(name = "last_booking_id")
    Booking lastBooking;
    @OneToOne
    @JoinColumn(name = "next_booking_id")
    Booking nextBooking;
}
