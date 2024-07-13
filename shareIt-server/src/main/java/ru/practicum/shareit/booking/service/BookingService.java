package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.exceptions.ItemIsUnAvailableException;
import ru.practicum.shareit.exceptions.UnknownStateException;

import java.util.List;

public interface BookingService {

    BookingDtoResponse createRequest(Long userId, BookingDto booking) throws EntityNotFoundException, ItemIsUnAvailableException;

    BookingDtoResponse updateState(Long userId, Long bookingId, String state) throws EntityNotFoundException, UnknownStateException; // подтверждение запроса на бронирование

    BookingDtoResponse get(Long userId, Long bookingId) throws EntityNotFoundException; // получение данных о конкретном бронировании только со стороны owner item или booker

    List<BookingDtoResponse> getAllUserBookings(Long userId, String state, Long from, Long size) throws EntityNotFoundException; // список бронирования вещей, которые забронил user

    List<BookingDtoResponse> getAllItemsBooked(Long userId, String state, Long from, Long size) throws EntityNotFoundException; // список забронированных вещей user-a

}
