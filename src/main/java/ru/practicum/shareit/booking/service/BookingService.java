package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {

    BookingDtoResponse createRequest(Long userId, BookingDto booking);

    BookingDtoResponse updateState(Long userId, Long bookingId, String state); // подтверждение запроса на бронирование

    BookingDtoResponse get(Long userId, Long bookingId); // получение данных о конкретном бронировании только со стороны owner item или booker

    List<BookingDtoResponse> getAllUserBookings(Long userId, String state, Long from, Long size); // список бронирования вещей, которые забронил user

    List<BookingDtoResponse> getAllItemsBooked(Long userId, String state, Long from, Long size); // список забронированных вещей user-a

}
