package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.exceptions.UnknownState;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(properties = "db.name = test")
public class ServiceBookingTests {
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserRepository userRepository;
    @Autowired
    BookingService service;
    User user;
    Item item;
    ItemDto itemDto;
    BookingDto bookingDto;
    Booking booking;

    @BeforeEach
    public void createUser() {
        user = new User();
        user.setId(1L);
        user.setEmail("email@name.ru");
        user.setName("name");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setAvailable(true);
        itemDto.setName("название");
        itemDto.setDescription("описание");

        itemDto.setComments(new ArrayList<>());

        item = Mapper.convertToItem(user.getId(), itemDto);
        item.setComments(new ArrayList<>());

        booking = new Booking();
        booking.setItem(item);
        booking.setId(1L);
        booking.setBooker(user);


        bookingDto = Mapper.convertToBookingDto(booking);
    }

    @Test
    public void createBookingTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        User user1 = new User();
        user1.setId(2L);
        user1.setName("name");
        user1.setEmail("new@mail.ru");

        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(1));
        service.createRequest(user1.getId(), bookingDto);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any(Booking.class));
    }

    @Test
    public void createBookingWithUnknownUserTest() {

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        User user1 = new User();
        user1.setId(2L);
        user1.setName("name");
        user1.setEmail("new@mail.ru");

        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(1));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.createRequest(user.getId(), bookingDto));
    }

    @Test
    public void createBookingWithUnknownItemTest() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        User user1 = new User();
        user1.setId(2L);
        user1.setName("name");
        user1.setEmail("new@mail.ru");

        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(1));

        Assertions.assertThrows(EntityNotFound.class, () -> service.createRequest(user.getId(), bookingDto));
    }

    @Test
    public void updateBookingStateTest() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.updateState(user.getId(), bookingDto.getId(), "true");

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any(Booking.class));

    }

    @Test
    public void getBookingTest() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.get(user.getId(), bookingDto.getId());

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(Mockito.anyLong());

    }

    @Test
    public void getAllUserBookingsTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "ALL", 0L ,10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerOrderByStartDesc(user, PageRequest.of(0, 10));

    }

    @Test
    public void getAllItemsBookedTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        List<Item> items = new ArrayList<>();

        items.add(item);

        Mockito.when(itemRepository.findAllByOwnerOrderById(Mockito.anyLong()))
                        .thenReturn(items);

        service.getAllItemsBooked(user.getId(), "ALL", 0L ,10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemInOrderByStartDesc(items, PageRequest.of(0, 10));

    }

    @Test
    public void getBookingByUnknownStateTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(UnknownState.class, () ->
                service.getAllUserBookings(user.getId(), "UNKNOWN", 0L ,10L));

    }
}
