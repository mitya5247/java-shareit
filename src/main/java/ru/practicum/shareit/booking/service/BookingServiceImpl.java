package ru.practicum.shareit.booking.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadState;
import ru.practicum.shareit.exceptions.BookingDtoIsNotValid;
import ru.practicum.shareit.exceptions.ItemIsUnAvailable;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
                new EntityNotFoundException("user c " + userId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow( () ->
            new EntityNotFoundException("item c " + bookingDto.getItemId() + " не найден"));
        if (!item.isAvailable()) {
            throw new ItemIsUnAvailable("item с id " + item.getId() + " недоступен для брони");
        }
        if (item.getOwner().equals(userId)) {
            throw new EntityNotFoundException("пользователь не может забронить свою вещь");
        }
        booking.setItem(item); // если он не owner доабвить if - else
        booking.setBooker(user);
        bookingRepository.save(booking);
        return Mapper.convertToBookingDtoResponse(booking);
    }

    @SneakyThrows
    @Override
    public BookingDtoResponse updateState(Long userId, Long bookingId, String state) {
        Booking booking = this.bookingNotFound(bookingId);
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new EntityNotFoundException("item c " + booking.getItem().getId() + " не найден"));
        if (!Objects.equals(item.getOwner(), userId)) {
            throw new EntityNotFoundException("user c id " + userId + " не может поменять статус владельца вещи с id " +
                    item.getOwner());
        }
        if (booking.getStatus().equals(State.APPROVED)) {
            throw new BadState("Бронь уже подтверждена");
        }
        switch (state) {
            case "true": booking.setStatus(State.APPROVED); // возможно применить break;
                bookingRepository.save(booking); // поменять статус вещи на занято после статуса
                this.setNextBooking(item, booking);
                itemRepository.save(item);
                List<Item> items = itemRepository.findAll();
                return Mapper.convertToBookingDtoResponse(booking);
            case "false" : booking.setStatus(State.REJECTED);
                bookingRepository.save(booking);
                return Mapper.convertToBookingDtoResponse(booking);
            default: throw new BadState("некоректный параметр state");
        }
    }

    @Override
    public BookingDtoResponse get(Long userId, Long bookingId) {
        Booking booking = this.bookingNotFound(bookingId);
        Item item = itemRepository.findById(booking.getItem().getId()).orElseThrow(() ->
                new EntityNotFoundException("item c " + booking.getItem().getId() + " не найден"));
        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(item.getOwner(), userId)) {
            throw new EntityNotFoundException("user c id " + userId + " не может просматривать статус запроса с id " +
                    booking.getId());
        }
        return Mapper.convertToBookingDtoResponse(booking);
    }

    @SneakyThrows
    @Override
    public List<BookingDtoResponse> getAllUserBookings(Long userId, String state) {
        List<Booking> bookings = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("user c " + userId + " не найден"));
        if (state == null) {
            state = String.valueOf(State.ALL);
        }
        List<Booking> bookingsAll = bookingRepository.findAll();
        bookings = this.chooseRequest(user, State.valueOf(state));
        return bookings.stream()
                .map(Mapper::convertToBookingDtoResponse)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public List<BookingDtoResponse> getAllItemsBooked(Long userId, String state) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("user c " + userId + " не найден"));
        List<Booking> bookings = new ArrayList<>();
        List<Booking> bookingsAll = bookingRepository.findAll();
        if (state == null) {
            state = String.valueOf(State.ALL);
        }
        List<Item> items = itemRepository.findAllByOwnerOrderById(userId);
        if (!items.isEmpty()) {
            bookings = this.chooseRequestForOwner(items, State.valueOf(state));
        }
        return bookings.stream()
                .map(Mapper::convertToBookingDtoResponse)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Booking bookingNotFound(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException
                ("booking c id " + bookingId + " не найден"));
    }

    private void validateTimeBookingDto(BookingDto bookingDto) throws BookingDtoIsNotValid {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BookingDtoIsNotValid("start и end запроса не может быть равен null");
        } else if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingDtoIsNotValid("start запроса не может быть равен end");
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now()) ||
                bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingDtoIsNotValid("end и start запроса не могут быть в прошлом");
        } else if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingDtoIsNotValid("end запроса не может раньше start");
        }
    }


    private List<Booking> chooseRequest(User user, State state) throws BadState {
        if (state == null) {
            state = State.ALL;
        }
        switch (state) {
            case CURRENT: return bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartAsc(user,
                    LocalDateTime.now(), LocalDateTime.now());
            case FUTURE: return bookingRepository.findAllByBookerAndEndAfterOrderByStartDesc(user, LocalDateTime.now());
            case PAST: return bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now());
            case APPROVED: return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.APPROVED, user);
            case REJECTED: return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.REJECTED, user);
            case WAITING: return bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.WAITING, user);
            case ALL: return bookingRepository.findAllByBookerOrderByStartDesc(user);
            default: throw new BadState("Неккоректный статус " + state);
        }
    }

    private List<Booking> chooseRequestForOwner(List<Item> items, State state) throws BadState {
        switch (state) {
            case CURRENT: return bookingRepository.findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(items,
                    LocalDateTime.now(), LocalDateTime.now());
            case FUTURE: return bookingRepository.findAllByItemInAndEndAfterOrderByStartDesc(items, LocalDateTime.now());
            case PAST: return bookingRepository.findAllByItemInAndEndBeforeOrderByStartDesc(items, LocalDateTime.now());
            case APPROVED: return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(State.APPROVED, items);
            case REJECTED: return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(State.REJECTED, items);
            case WAITING: return bookingRepository.findAllByStatusAndItemInOrderByStartDesc(State.WAITING, items);
            case ALL: return bookingRepository.findAllByItemInOrderByStartDesc(items);
            default: throw new BadState("Неккоректный статус " + state);
        }
    }

    private Item setNextBooking(Item item, Booking booking) {
        if (item.getNextBooking() == null && item.getLastBooking() == null) {
            item.setNextBooking(booking);
            item.setLastBooking(booking);
            return item;
        }
        this.synchronizeTime(item);
        if (item.getNextBooking().equals(item.getLastBooking())) {
            item.setNextBooking(booking);
            return item;
        } else {
            return item;
        }
   //     item.setLastBooking(item.getNextBooking());
    //    item.setNextBooking(booking);
    }

    private Item synchronizeTime(Item item) {
        LocalDateTime moment = LocalDateTime.now();
        if (item.getNextBooking().getStart().isBefore(moment)) {
            item.setLastBooking(item.getNextBooking());
        }
        return item;
    }
}
