package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByStatusAndBookerOrderByStartDesc(State state, User user, Pageable pageable);

    List<Booking> findAllByStatusAndItemInOrderByStartDesc(State state, List<Item> items, Pageable pageable);

    List<Booking> findAllByBookerOrderByStartDesc(User user, Pageable pageable);

    List<Booking> findAllByBookerAndEndAfterOrderByStartDesc(User user, LocalDateTime moment, Pageable pageable);

    List<Booking> findAllByBookerAndEndBeforeOrderByStartDesc(User user, LocalDateTime moment, Pageable pageable);

    List<Booking> findAllByItemInAndEndAfterOrderByStartDesc(List<Item> items, LocalDateTime moment, Pageable pageable);

    List<Booking> findAllByItemInAndEndBeforeOrderByStartDesc(List<Item> items, LocalDateTime moment, Pageable pageable);

    List<Booking> findAllByItemInOrderByStartDesc(List<Item> items, Pageable pageable);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartAsc(User user, LocalDateTime now,
                                                                          LocalDateTime now1, Pageable pageable);

    List<Booking> findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(List<Item> items, LocalDateTime now,
                                                                           LocalDateTime now1, Pageable pageable);

    Booking findFirstByItemAndStartBetweenOrderByStartDesc(Item item, LocalDateTime moment, LocalDateTime moment1);

    Booking findFirstByBookerAndItemOrderByStart(User user, Item item);

    List<Booking> findAllByBookerOrderByStartDesc(User user);

    List<Booking> findAllByBooker(User user, Pageable pageable);


}
