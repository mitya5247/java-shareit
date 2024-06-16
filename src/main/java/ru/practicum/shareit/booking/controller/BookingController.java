package ru.practicum.shareit.booking.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

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
    public BookingDtoResponse createRequest(@RequestHeader(Constants.HEADER) Long userId, @RequestBody BookingDto bookingDto) {
        return service.createRequest(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse updateState(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long bookingId,
                        @RequestParam(name = "approved") String state) {
        return service.updateState(userId, bookingId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse get(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long bookingId) {
        return service.get(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoResponse> getAllUserBookings(@RequestHeader(Constants.HEADER) Long userId,
                                     @RequestParam(name = "state", required = false) String state) {
        return service.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllItemsBooked(@RequestHeader(Constants.HEADER) Long userId,
                                    @RequestParam(name = "state", required = false) String state) {
        return service.getAllItemsBooked(userId, state);
    }


}
