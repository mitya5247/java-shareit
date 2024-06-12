package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    User booker;
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
    @Column(name = "status")
    State status = State.WAITING;
    @Column(name = "start_time")
    Timestamp start;
    @Column(name = "end_time")
    Timestamp end;
}
