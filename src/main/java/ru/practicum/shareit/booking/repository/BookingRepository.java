package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByStatusAndBookerOrderByStartDesc(String state, Long userId);

 //   List<Booking> findAllByOwnerAndStateOrderByDesc(String state, Long userId);

}
