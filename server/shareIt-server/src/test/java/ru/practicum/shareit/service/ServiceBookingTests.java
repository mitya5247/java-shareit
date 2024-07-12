package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

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
    public void createBookingTest() throws EntityNotFoundException, ItemIsUnAvailableException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        User user1 = new User();
        user1.setId(2L);
        user1.setName("name");
        user1.setEmail("new@mail.ru");

        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(2));
        service.createRequest(user1.getId(), bookingDto);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any(Booking.class));
    }

    @Test
    public void createBookingOnAvailableItemTest() {

        item.setAvailable(false);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(2));

        Assertions.assertThrows(ItemIsUnAvailableException.class, () -> service.createRequest(user.getId(), bookingDto));


    }

    @Test
    public void createBookingOnOwnerItemTest() {

        item.setOwner(user.getId());

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(2));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.createRequest(user.getId(), bookingDto));


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
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(2));

        Assertions.assertThrows(javax.persistence.EntityNotFoundException.class, () -> service.createRequest(user.getId(), bookingDto));
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
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(2));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.createRequest(user.getId(), bookingDto));
    }

    @Test
    public void updateBookingStateTest() throws EntityNotFoundException, UnknownStateException {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.updateState(user.getId(), bookingDto.getId(), "true");

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any(Booking.class));

    }

    @Test
    public void updateBookingStateWithByUnknownItemTest() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.updateState(user.getId(), bookingDto.getId(), "true"));
    }

    @Test
    public void updateBookingStateWithAlreadyACCEPTEDTest() {
        booking.setStatus(State.APPROVED);

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(UnknownStateException.class, () -> service.updateState(user.getId(), bookingDto.getId(), "true"));
    }

    @Test
    public void updateBookingStateWithByOtherUserTest() {
        booking.setStatus(State.APPROVED);
        user.setId(2L);

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.updateState(user.getId(), bookingDto.getId(), "true"));
    }

    @Test
    public void updateBookingStateREJECTEDTest() throws EntityNotFoundException, UnknownStateException {

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.updateState(user.getId(), bookingDto.getId(), "false");

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any(Booking.class));

    }

    @Test
    public void getBookingTest() throws EntityNotFoundException {
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.get(user.getId(), bookingDto.getId());

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(Mockito.anyLong());

    }

    @Test
    public void getBookingByUnknownItemTest() {
        user.setId(30L);
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.get(user.getId(), bookingDto.getId()));

    }

    @Test
    public void getBookingByUnknownUserTest() {
        user.setId(30L);
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.get(user.getId(), bookingDto.getId()));

    }

    @Test
    public void getAllUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "ALL", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerOrderByStartDesc(user, PageRequest.of(0, 10));

    }

    @Test
    public void getCURRENTUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "CURRENT", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerAndStartBeforeAndEndAfterOrderByStartAsc(Mockito.any(User.class), Mockito.any(LocalDateTime.class),
                        Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));

    }

    @Test
    public void getFUTUREUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "FUTURE", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerAndEndAfterOrderByStartDesc(Mockito.any(User.class), Mockito.any(LocalDateTime.class),
                        Mockito.any(Pageable.class));

    }

    @Test
    public void getPASTUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "PAST", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerAndEndBeforeOrderByStartDesc(Mockito.any(User.class), Mockito.any(LocalDateTime.class),
                        Mockito.any(Pageable.class));

    }

    @Test
    public void getAPPROVEDUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "APPROVED", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByStatusAndBookerOrderByStartDesc(Mockito.any(), Mockito.any(User.class),
                        Mockito.any(Pageable.class));

    }

    @Test
    public void getREJECTEDUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "REJECTED", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByStatusAndBookerOrderByStartDesc(Mockito.any(), Mockito.any(User.class),
                        Mockito.any(Pageable.class));

    }

    @Test
    public void getWAITINGUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), "WAITING", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByStatusAndBookerOrderByStartDesc(Mockito.any(), Mockito.any(User.class),
                        Mockito.any(Pageable.class));

    }

    @Test
    public void getUserBookingsByUnknownUserTest() {

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getAllUserBookings(user.getId(), null, 0L, 10L));

    }

    @Test
    public void getNullStateUserBookingsTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        service.getAllUserBookings(user.getId(), null, 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerOrderByStartDesc(Mockito.any(User.class),
                        Mockito.any(Pageable.class));

    }

    @Test
    public void getStateUserBookingsWithNegativeFromTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(IllegalArgumentException.class, () ->  service.getAllUserBookings(user.getId(), null, -1L, 10L));

    }

    @Test
    public void getAllUserBookingsWithNegativeSizeTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(IllegalArgumentException.class, () ->  service.getAllUserBookings(user.getId(), null, 0L, -10L));

    }

    @Test
    public void getAllItemsBookedTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), "ALL", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemInOrderByStartDesc(items, PageRequest.of(0, 10));

    }

    @Test
    public void getAllItemsBookedByUnknownUserTest() {

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        List<Item> items = new ArrayList<>();

        items.add(item);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getAllItemsBooked(user.getId(), "ALL", 0L, 10L));

    }

    @Test
    public void getAllItemsBookedByNegativeFromTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        List<Item> items = new ArrayList<>();

        items.add(item);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getAllItemsBooked(user.getId(), "ALL", -1L, 10L));

    }

    @Test
    public void getAllItemsBookedByNegativeSizeTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        List<Item> items = new ArrayList<>();

        items.add(item);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getAllItemsBooked(user.getId(), "ALL", 0L, -10L));

    }

//    @Test
//    public void getAllItemsBookedByUnknownStateTest() {
//        Mockito.when(userRepository.findById(Mockito.anyLong()))
//                .thenReturn(Optional.ofNullable(user));
//        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
//                .thenReturn(Optional.ofNullable(booking));
//        Mockito.when(itemRepository.findById(Mockito.anyLong()))
//                .thenReturn(Optional.ofNullable(item));
//
//        List<Item> items = new ArrayList<>();
//
//        items.add(item);
//
//        Mockito.when(itemRepository.findAllByOwnerOrderById(Mockito.anyLong()))
//                .thenReturn(items);
//
//        Assertions.assertThrows(UnknownStateException.class, () -> service.getAllItemsBooked(user.getId(), "bla", 0L, 10L));
//
//    }

    @Test
    public void getAllItemsBookedByNullStateTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), null, 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemInOrderByStartDesc(items, PageRequest.of(0, 10));

    }

    @Test
    public void getPASTItemsBookedTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), "PAST", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemInAndEndBeforeOrderByStartDesc(Mockito.anyList(), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));

    }

    @Test
    public void getFUTUREItemsBookedTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), "FUTURE", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemInAndEndAfterOrderByStartDesc(Mockito.anyList(), Mockito.any(LocalDateTime.class), Mockito.any(Pageable.class));

    }

    @Test
    public void getAPPROVEDItemsBookedTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), "APPROVED", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByStatusAndItemInOrderByStartDesc(Mockito.any(), Mockito.anyList(), Mockito.any(Pageable.class));

    }

    @Test
    public void getREJECTEDItemsBookedTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), "REJECTED", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByStatusAndItemInOrderByStartDesc(Mockito.any(), Mockito.anyList(), Mockito.any(Pageable.class));

    }

    @Test
    public void getWAITINGItemsBookedTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), "WAITING", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByStatusAndItemInOrderByStartDesc(Mockito.any(), Mockito.anyList(), Mockito.any(Pageable.class));

    }

    @Test
    public void getCURRENTItemsBookedTest() throws EntityNotFoundException {
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

        service.getAllItemsBooked(user.getId(), "CURRENT", 0L, 10L);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(Mockito.anyList(),
                        Mockito.any(), Mockito.any(), Mockito.any(Pageable.class));

    }

//    @Test
//    public void getBookingByUnknownStateTest() {
//        Mockito.when(userRepository.findById(Mockito.anyLong()))
//                .thenReturn(Optional.ofNullable(user));
//        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
//                .thenReturn(Optional.ofNullable(booking));
//        Mockito.when(itemRepository.findById(Mockito.anyLong()))
//                .thenReturn(Optional.ofNullable(item));
//
//        Assertions.assertThrows(UnknownStateException.class, () ->
//                service.getAllUserBookings(user.getId(), "UNKNOWN", 0L, 10L));
//
//    }
}
