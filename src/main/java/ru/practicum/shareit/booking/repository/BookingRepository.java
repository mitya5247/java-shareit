package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByStatusAndBookerOrderByStartDesc(State state, User user);

    List<Booking> findAllByStatusAndItemInOrderByStartDesc(State state, List<Item> items);

    List<Booking> findAllByBookerOrderByStartDesc(User user);

    List<Booking> findAllByBookerAndEndAfterOrderByStartDesc(User user, LocalDateTime moment);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(User user, LocalDateTime moment);

    List<Booking> findAllByItemInAndEndAfterOrderByStartDesc(List<Item> items, LocalDateTime moment);

    List<Booking> findAllByItemInAndEndBeforeOrderByStartDesc(List<Item> items, LocalDateTime moment);

    List<Booking> findAllByItemInOrderByStartDesc(List<Item> items);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartAsc(User user, LocalDateTime now, LocalDateTime now1); // новый

    List<Booking> findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(List<Item> items, LocalDateTime now,
                                                                           LocalDateTime now1); // новый

//    List<Booking> findAllByItemAndStatus(Item item, State state);

 //   Booking findByItemAndEndBeforeOrderByStartDesc(Item item, LocalDateTime moment); // PAST
 //   Booking findByItemAndEndAfterOrderByStartDesc(Item item, LocalDateTime moment); // FUTURE
  //  Booking findByItemAndStartBetween(Item item, LocalDateTime moment, LocalDateTime moment1);

    Booking findFirstByItemAndStartBetweenOrderByStartDesc(Item item, LocalDateTime moment, LocalDateTime moment1);

    Booking findFirstByBookerAndItemOrderByStart(User user, Item item);


}
