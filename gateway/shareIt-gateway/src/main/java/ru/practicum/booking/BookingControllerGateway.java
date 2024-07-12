package ru.practicum.booking;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.State;
import ru.practicum.exceptions.BookingDtoIsNotValidException;
import ru.practicum.exceptions.UnknownStateException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = Constants.PATH_BOOKINGS)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingControllerGateway {

    @Autowired
    BookingClient client;

    String baseUri;

    public BookingControllerGateway(BookingClient client, @Value("${server-uri}") String baseUri) {
        this.client = client;
        this.baseUri = baseUri + Constants.PATH_BOOKINGS;
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(Constants.HEADER) Long userId, @Valid @RequestBody BookingDto bookingDto) throws BookingDtoIsNotValidException {
        this.validateTimeBookingDto(bookingDto);
        return client.post(baseUri, userId, bookingDto);
    }

    @PatchMapping(Constants.PATH_BOOKING_ID)
    public ResponseEntity<Object> updateState(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long bookingId,
                                          @RequestParam(name = "approved") String state) throws UnknownStateException {
        if (state.equals("true") || state.equals("false")) {
            String uri = baseUri + "/" + bookingId + "?approved=" + state;
            return client.patch(uri, userId);
        } else {
            throw new UnknownStateException("Unknown state: " + state);
        }
    }

    @GetMapping(Constants.PATH_BOOKING_ID)
    public ResponseEntity<Object> get(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long bookingId) {
        String uri = baseUri + "/" + bookingId;
        return client.get(uri, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBookings(@RequestHeader(Constants.HEADER) Long userId,
                                                       @RequestParam(name = "state", required = false) String state, @RequestParam(required = false,
            defaultValue = "0") Long from, @RequestParam(required = false, defaultValue = "10") Long size) throws UnknownStateException {
        if (state != null) {
            this.validateState(state);
        }
        String uri = this.buildUri(state, from, size);
        return client.get(uri, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllItemsBooked(@RequestHeader(Constants.HEADER) Long userId,
                                                      @RequestParam(name = "state", required = false) String state, @RequestParam(required = false,
            defaultValue = "0") Long from, @RequestParam(required = false, defaultValue = "10") Long size) throws UnknownStateException {
        if (state != null) {
            this.validateState(state);
        }
        String uri = this.buildUriOwner(state, from, size);
        return client.get(uri, userId);
    }

    private String buildUri(String state, Long from, Long size) {
        String uri = "";
        if (state == null) {
            uri = baseUri + "/?from=" + from + "&size=" + size;
        } else {
            uri = baseUri + "/?state=" + state + "&from=" + from + "&size=" + size;
        }
        return uri;
    }

    private String buildUriOwner(String state, Long from, Long size) {
        String uri = "";
        if (state == null) {
            uri = baseUri + "/owner/?from=" + from + "&size=" + size;
        } else {
            uri = baseUri + "/owner/?state=" + state + "&from=" + from + "&size=" + size;
        }
        return uri;
    }

    private void validateTimeBookingDto(BookingDto bookingDto) throws BookingDtoIsNotValidException {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BookingDtoIsNotValidException("start and end time couldn't be null");
        } else if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingDtoIsNotValidException("start couldn't be equals end");
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now()) ||
                bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDtoIsNotValidException("end and start time couldn't be in past");
        } else if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingDtoIsNotValidException("end couldn't be earlier than start");
        }
    }

    private State validateState(String state) throws UnknownStateException {
        State state1;
        try {
            state1 = State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new UnknownStateException("Unknown state: " + state);
        }
        return state1;
    }
}
