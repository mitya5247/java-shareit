package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    Booking createRequest(Long userId, BookingDto booking);

    Booking updateState(Long userId, Long bookingId, String state); // подтверждение запроса на бронирование

    Booking get(Long userId, Long bookingId); // получение данных о конкретном бронировании только со стороны owner item или booker

    List<Booking> getAllUserBookings(Long userId, String state); // список бронирования вещей, которые забронил user

    List<Booking> getAllItemsBooked(Long userId, String state); // список забронированных вещей user-a

}
