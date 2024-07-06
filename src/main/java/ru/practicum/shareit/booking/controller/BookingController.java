package ru.practicum.shareit.booking.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BookingDtoIsNotValidException;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.exceptions.ItemIsUnAvailableException;
import ru.practicum.shareit.exceptions.UnknownStateException;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BookingController {

    BookingService service;

    @PostMapping
    public BookingDtoResponse createRequest(@RequestHeader(Constants.HEADER) Long userId, @Valid @RequestBody BookingDto bookingDto) throws EntityNotFoundException, ItemIsUnAvailableException, BookingDtoIsNotValidException {
        return service.createRequest(userId, bookingDto);
    }

    @PatchMapping(Constants.PATH_BOOKING_ID)
    public BookingDtoResponse updateState(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long bookingId,
                                          @RequestParam(name = "approved") String state) throws EntityNotFoundException, UnknownStateException {
        return service.updateState(userId, bookingId, state);
    }

    @GetMapping(Constants.PATH_BOOKING_ID)
    public BookingDtoResponse get(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long bookingId) throws EntityNotFoundException {
        return service.get(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoResponse> getAllUserBookings(@RequestHeader(Constants.HEADER) Long userId,
                                                       @RequestParam(name = "state", required = false) String state, @RequestParam(required = false,
            defaultValue = "0") Long from, @RequestParam(required = false, defaultValue = "10") Long size) throws EntityNotFoundException, UnknownStateException {
        return service.getAllUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllItemsBooked(@RequestHeader(Constants.HEADER) Long userId,
                                                      @RequestParam(name = "state", required = false) String state, @RequestParam(required = false,
            defaultValue = "0") Long from, @RequestParam(required = false, defaultValue = "10") Long size) throws EntityNotFoundException, UnknownStateException {
        return service.getAllItemsBooked(userId, state, from, size);
    }


}
