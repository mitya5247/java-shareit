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
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemIsUnAvailable;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
    public Booking createRequest(Long userId, BookingDto bookingDto) {
     //   booking.getBooker().setId(userId);
        Booking booking = Mapper.convertToBooking(userId, bookingDto);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("user c " + userId + " не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow( () ->
            new EntityNotFoundException("item c " + bookingDto.getItemId() + " не найден"));
        if (!item.isAvailable()) {
            throw new ItemIsUnAvailable("item с id " + item.getId() + " недоступен для брони");
        }
        booking.setItem(item);
        booking.setBooker(user);
        bookingRepository.save(booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateState(Long userId, Long bookingId, String state) {
        Booking booking = this.bookingNotFound(bookingId);
        switch (state) {
            case "true": booking.setStatus(State.APPROVED); // возможно применить break;
                bookingRepository.save(booking);
                return booking;
            case "false" : booking.setStatus(State.REJECTED);
                bookingRepository.save(booking);
                return booking;
            default: log.info("некоректный параметр state");
        }
        return booking;
    }

    @Override
    public Booking get(Long userId, Long bookingId) {
        Booking booking = this.bookingNotFound(bookingId);
        return booking;
    }

    @Override
    public List<Booking> getAllUserBookings(Long userId, String state) {
        List<Booking> bookings = new ArrayList<>();
        if (state.equals(State.ALL.toString()) || state.equals(State.PAST.toString()) ||
                state.equals(State.FUTURE.toString()) || state.equals(State.CURRENT.toString()) ||
                state.equals(State.WAITING.toString()) || state.equals(State.APPROVED.toString()) ||
                state.equals(State.REJECTED.toString())) {
            bookings = bookingRepository.findAllByStatusAndBookerOrderByStartDesc(state, userId);
        } else {
            log.info("некорректный параметр state " + state);
        }
        return bookings;
    }

    @Override
    public List<Booking> getAllItemsBooked(Long userId, String state) {
        List<Booking> bookings = new ArrayList<>();
        return bookings;
    }

    @SneakyThrows
    private Booking bookingNotFound(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new EntityNotFoundException
                ("booking c id " + bookingId + " не найден"));
    }
}
