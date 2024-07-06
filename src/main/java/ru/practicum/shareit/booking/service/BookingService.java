package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.exceptions.BookingDtoIsNotValid;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.exceptions.ItemIsUnAvailable;
import ru.practicum.shareit.exceptions.UnknownState;

import java.util.List;

public interface BookingService {

    BookingDtoResponse createRequest(Long userId, BookingDto booking) throws EntityNotFound, ItemIsUnAvailable, BookingDtoIsNotValid;

    BookingDtoResponse updateState(Long userId, Long bookingId, String state) throws EntityNotFound, UnknownState; // подтверждение запроса на бронирование

    BookingDtoResponse get(Long userId, Long bookingId) throws EntityNotFound; // получение данных о конкретном бронировании только со стороны owner item или booker

    List<BookingDtoResponse> getAllUserBookings(Long userId, String state, Long from, Long size) throws UnknownState, EntityNotFound; // список бронирования вещей, которые забронил user

    List<BookingDtoResponse> getAllItemsBooked(Long userId, String state, Long from, Long size) throws EntityNotFound, UnknownState; // список забронированных вещей user-a

}
