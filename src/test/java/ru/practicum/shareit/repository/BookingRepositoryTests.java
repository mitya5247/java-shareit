package ru.practicum.shareit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest(properties = "db.name = test")
public class BookingRepositoryTests {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    EntityManager em;
    User user;
    Item item;
    ItemDto itemDto;
    Booking booking;
    BookingDto bookingDto;
    Long bookingId;
    Long userId;
    Long itemId;

    @BeforeEach
    public void initBooking() {
        user = new User();
        user.setEmail("email@name.ru");
        user.setName("name");

        userId = userRepository.save(user).getId();

        itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setName("название");
        itemDto.setDescription("описание");

        itemDto.setComments(new ArrayList<>());

        item = Mapper.convertToItem(user.getId(), itemDto);
        item.setComments(new ArrayList<>());

        itemId = itemRepository.save(item).getId();

        bookingDto = new BookingDto();

        bookingDto.setItemId(item.getId());
        bookingDto.setBookerId(user.getId());

        booking = Mapper.convertToBooking(bookingDto);
        booking.setItem(item);
        booking.setBooker(user);

        booking.setStart(LocalDateTime.now().plusSeconds(1));
        booking.setEnd(LocalDateTime.now().plusHours(12));

        bookingId = bookingRepository.save(booking).getId();
    }

    @Test
    public void createBooking() {

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where b.id = :id", Booking.class)
                        .setParameter("id", bookingId);
        Booking booking1 = query.getSingleResult();

        Assertions.assertEquals(bookingId, booking1.getId());
        Assertions.assertEquals(booking.getItem().getId(), booking1.getItem().getId());
        Assertions.assertEquals(booking.getEnd(), booking1.getEnd());
        Assertions.assertEquals(booking.getStart(), booking1.getStart());
        Assertions.assertEquals(booking.getBooker(), booking1.getBooker());

    }

    @Test
    public void findAllByStatusWAITINGAndBookerTest() {

        List<Booking> bookings = bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.WAITING, user,
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByStatusAPPROVEDAndBookerTest() {

        booking.setStatus(State.APPROVED);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.APPROVED, user,
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }


    @Test
    public void findAllByStatusREJECTEDAndBookerTest() {

        booking.setStatus(State.REJECTED);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findAllByStatusAndBookerOrderByStartDesc(State.REJECTED, user,
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByBookerOrderByStartDescTest() {

        List<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDesc(user,
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByBookerAndEndAfterOrderByStartDescTest() {

        List<Booking> bookings = bookingRepository.findAllByBookerAndEndAfterOrderByStartDesc(user, LocalDateTime.now(),
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByBookerAndEndBeforeOrderByStartDescTest() {

        List<Booking> bookings = bookingRepository.findAllByBookerAndEndBeforeOrderByStartDesc(user, LocalDateTime.now().plusDays(1),
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByBookerAndStartBeforeAndEndAfterOrderByStartAscTest() {

        List<Booking> bookings = bookingRepository.findAllByBookerAndStartBeforeAndEndAfterOrderByStartAsc(user, LocalDateTime.now().plusHours(12),
                LocalDateTime.now().plusSeconds(2), PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findFirstByBookerAndItemOrderByStartTest() {

        Booking booking1 = bookingRepository.findFirstByBookerAndItemOrderByStart(user, item);

        Assertions.assertEquals(bookingId, booking1.getId());
        Assertions.assertEquals(booking.getItem().getId(), booking1.getItem().getId());
        Assertions.assertEquals(booking.getEnd(), booking1.getEnd());
        Assertions.assertEquals(booking.getStart(), booking1.getStart());
        Assertions.assertEquals(booking.getBooker(), booking1.getBooker());

    }

    @Test
    public void findAllByStatusAndItemTest() {

        List<Item> items = new ArrayList<>();
        items.add(item);

        List<Booking> bookings = bookingRepository.findAllByStatusAndItemInOrderByStartDesc(State.WAITING, items,
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByItemInAndEndAfterOrderByStartDescTest() {

        List<Item> items = new ArrayList<>();
        items.add(item);

        List<Booking> bookings = bookingRepository.findAllByItemInAndEndAfterOrderByStartDesc(items, LocalDateTime.now(),
                PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByItemInAndEndBeforeOrderByStartDescTest() {

        List<Item> items = new ArrayList<>();
        items.add(item);

        List<Booking> bookings = bookingRepository.findAllByItemInAndEndBeforeOrderByStartDesc(items, LocalDateTime.now()
                        .plusMonths(1), PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByItemInOrderByStartDescTest() {

        List<Item> items = new ArrayList<>();
        items.add(item);

        List<Booking> bookings = bookingRepository.findAllByItemInOrderByStartDesc(items, PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findAllByItemInAndStartBeforeAndEndAfterOrderByStartDescTest() {

        List<Item> items = new ArrayList<>();
        items.add(item);

        List<Booking> bookings = bookingRepository.findAllByItemInAndStartBeforeAndEndAfterOrderByStartDesc(items,
                LocalDateTime.now().plusHours(12), LocalDateTime.now(), PageRequest.of(0, 10));

        Assertions.assertEquals(bookingId, bookings.get(0).getId());
        Assertions.assertEquals(booking.getItem().getId(), bookings.get(0).getItem().getId());
        Assertions.assertEquals(booking.getEnd(), bookings.get(0).getEnd());
        Assertions.assertEquals(booking.getStart(), bookings.get(0).getStart());
        Assertions.assertEquals(booking.getBooker(), bookings.get(0).getBooker());

    }

    @Test
    public void findFirstByItemAndStartBetweenOrderByStartDescTest() {

        Booking booking1 = bookingRepository.findFirstByItemAndStartBetweenOrderByStartDesc(item,
                LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));

        Assertions.assertEquals(bookingId, booking1.getId());
        Assertions.assertEquals(booking.getItem().getId(), booking1.getItem().getId());
        Assertions.assertEquals(booking.getEnd(), booking1.getEnd());
        Assertions.assertEquals(booking.getStart(), booking1.getStart());
        Assertions.assertEquals(booking.getBooker(), booking1.getBooker());

    }

    @AfterEach
    public void deleteAll() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}
