package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @SneakyThrows
    @Override
    public BookingDtoResponse createRequest(Long userId, BookingDto bookingDto) {
        this.validateTimeBookingDto(bookingDto);
        Booking booking = Mapper.convertToBooking(bookingDto);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("user with id " + userId + " was not found"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() ->
                new EntityNotFound("item with id " + bookingDto.getItemId() + " не найден"));
        if (!item.isAvailable()) {
            throw new ItemIsUnAvailable("item with id " + item.getId() + " is not available for booking");
        }
        if (item.getOwner().equals(userId)) {
            throw new EntityNotFound("user couldn't book own item");
        }
        booking.setItem(item);
        booking.setBooker(user);
        bookingRepository.save(booking);
        return Mapper.convertToBookingDtoResponse(booking);
    }

    @SneakyThrows
    @Override
    public BookingDtoResponse updateState(Long userId, Long bookingId, String state) {
        Booking booking = this.bookingNotFound(bookingId);
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new EntityNotFound("item with id " + booking.getItem().getId() + " was not found"));
        if (!Objects.equals(item.getOwner(), userId)) {
            throw new EntityNotFound("user with id " + userId + " couldn't change owner's status of item with id " +
                    item.getOwner());
        }
        if (booking.getStatus().equals(State.APPROVED)) {
            throw new UnknownState("booking is already accepted");
        }
        switch (state) {
            case "true":
                booking.setStatus(State.APPROVED);
                bookingRepository.save(booking);
                itemRepository.save(item);
                return Mapper.convertToBookingDtoResponse(booking);
            case "false":
                booking.setStatus(State.REJECTED);
                bookingRepository.save(booking);
                return Mapper.convertToBookingDtoResponse(booking);
            default:
                throw new UnknownState("invalid state");
        }
    }

    @SneakyThrows
    @Override
    public BookingDtoResponse get(Long userId, Long bookingId) {
        Booking booking = this.bookingNotFound(bookingId);
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new EntityNotFound("item with id " + booking.getItem().getId() + " was not found"));
        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(item.getOwner(), userId)) {
            throw new EntityNotFound("user with id " + userId + " не может просматривать статус запроса с id " +
                    booking.getId());
        }
        return Mapper.convertToBookingDtoResponse(booking);
    }

    @SneakyThrows
    @Override
    public List<BookingDtoResponse> getAllUserBookings(Long userId, String state, Long from, Long size) {
        List<Booking> bookings;
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFound("user with id " + userId + " was not found"));
        if (from < 0) {
            throw new IllegalArgumentException("from couldn't be less 0 " + from);
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size couldn't be less 0 " + from);
        }
        if (state == null) {
            state = String.valueOf(State.ALL);
        }
        try {
            bookings = this.chooseRequest(user, State.valueOf(state), from, size);
        } catch (IllegalArgumentException e) {
            throw new UnknownState("Unknown state: " + state);
        }
        return bookings.stream()
                .map(Mapper::convertToBookingDtoResponse)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List<BookingDtoResponse> getAllItemsBooked(Long userId, String state, Long from, Long size) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFound("user with id " + userId + " was not found"));
        List<Booking> bookings = new ArrayList<>();
        if (from < 0) {
            throw new IllegalArgumentException("from couldn't be less 0 " + from);
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size couldn't be less 0 " + from);
        }
        if (state == null) {
            state = String.valueOf(State.ALL);
        }
        List<Item> items = itemRepository.findAllByOwnerOrderById(userId);
        if (!items.isEmpty()) {
            try {
                bookings = this.chooseRequestForOwner(items, State.valueOf(state), from, size);
            } catch (IllegalArgumentException e) {
                throw new UnknownState("Unknown state: " + state);
            }
        }
        return bookings.stream()
                .map(Mapper::convertToBookingDtoResponse)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Booking bookingNotFound(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new ItemNotFound(
                "booking with id " + bookingId + " was not found"));
    }

    private void validateTimeBookingDto(BookingDto bookingDto) throws BookingDtoIsNotValid {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BookingDtoIsNotValid("start and end time couldn't be null");
        } else if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingDtoIsNotValid("start couldn't be equals end");
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now()) ||
                bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDtoIsNotValid("end and start time couldn't be in past");
        } else if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingDtoIsNotValid("end couldn't be earlier than start");
        }
    }


    @SneakyThrows
    private List<Booking> chooseRequest(User user, State state, Long from, Long size) {
        int fromInt = Integer.parseInt(from.toString());
        int sizeInt = Integer.parseInt(size.toString());
        Pageable page = PageRequest.of(fromInt / sizeInt, sizeInt);
        if (state == null) {
            state = State.ALL;
        }
        switch (state) {
            case CURRENT:
                return bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartAsc(user,
                        LocalDateTime.now(), LocalDateTime.now(), page);
            case FUTURE:
                return bookingRepository.findAllByBookerAndEndAfterOrderByStartDesc(user, LocalDateTime.now(), page);
            case PAST:
                return bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now(), page);
            case APPROVED:
                return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.APPROVED, user, page);
            case REJECTED:
                return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.REJECTED, user, page);
            case WAITING:
                return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.WAITING, user, page);
            case ALL:
                return bookingRepository.findAllByBookerOrderByStartDesc(user, page);
            default:
                throw new UnknownState("UnknownState: " + state);
        }
    }

    @SneakyThrows
    private List<Booking> chooseRequestForOwner(List<Item> items, State state, Long from, Long size) {
        int fromInt = Integer.parseInt(from.toString());
        int sizeInt = Integer.parseInt(size.toString());
        Pageable page = PageRequest.of(fromInt / sizeInt, sizeInt);

        switch (state) {
            case CURRENT:
                return bookingRepository.findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(items,
                        LocalDateTime.now(), LocalDateTime.now(), page);
            case FUTURE:
                return bookingRepository.findAllByItemInAndEndAfterOrderByStartDesc(items, LocalDateTime.now(), page);
            case PAST:
                return bookingRepository.findAllByItemInAndEndBeforeOrderByStartDesc(items, LocalDateTime.now(), page);
            case APPROVED:
                return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(State.APPROVED, items, page);
            case REJECTED:
                return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(State.REJECTED, items, page);
            case WAITING:
                return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(State.WAITING, items, page);
            case ALL:
                return bookingRepository.findAllByItemInOrderByStartDesc(items, page);
            default:
                throw new UnknownState("UnknownState: " + state);
        }
    }

}
