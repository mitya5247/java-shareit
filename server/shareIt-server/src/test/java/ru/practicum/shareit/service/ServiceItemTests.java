package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadCommentException;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(properties = "db.name = test")
public class ServiceItemTests {
    @MockBean
    ItemRepository itemRepository;
    @Autowired
    ItemService service;
    @MockBean
    UserRepository userRepository;
    ItemDto itemDto;
    Item item;
    User user;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    CommentRepository commentRepository;

    @BeforeEach
    public void createUserAndItem() {
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
    }

    @Test
    public void createItemTest() throws EntityNotFoundException {

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        service.add(user.getId(), itemDto);

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(Mockito.any(Item.class));

    }

    @Test
    public void createItemWithUnknownRequestTest() {

        itemDto.setRequestId(5L);

        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.add(user.getId(), itemDto));

    }

    @Test
    public void createItemByUnknownUserTest() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.add(user.getId(), itemDto));

    }


    @Test
    public void updateItemTest() throws EntityNotFoundException {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        service.update(user.getId(), itemDto.getId(), itemDto);

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(Mockito.any(Item.class));

    }

    @Test
    public void updateByNotOwnerItemTest() {

        user.setId(3L);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(user.getId(), itemDto.getId(), itemDto));

    }

    @Test
    public void getItemTest() throws EntityNotFoundException {

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        service.get(user.getId(), itemDto.getId());

        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(itemDto.getId());

    }

    @Test
    public void getItemByOtherUserTest() throws EntityNotFoundException {

        user.setId(3L);

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item);

        service.get(user.getId(), itemDto.getId());

        Assertions.assertNull(itemDto.getNextBooking());
        Assertions.assertNull(itemDto.getLastBooking());

    }

    @Test
    public void getUnknownItemTest() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.get(user.getId(), 100L));
        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(Mockito.anyLong());
    }


    @Test
    public void getAllItemsTest() {
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        List<Item> items = new ArrayList<>();
        items.add(item);

        Mockito.when(itemRepository.findAllByOwnerOrderById(user.getId()))
                .thenReturn(items);

        service.getAll(user.getId());

        Mockito.verify(itemRepository, Mockito.times(1))
                .findAllByOwnerOrderById(user.getId());

    }

    @Test
    public void getAllItemsByNullNextBookingTest() {
        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        List<Item> items = new ArrayList<>();
        items.add(item);

        Mockito.when(itemRepository.findAllByOwnerOrderById(user.getId()))
                .thenReturn(items);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);

        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(124));

        Mockito.when(bookingRepository.findFirstByItemAndStartBetweenOrderByStartDesc(Mockito.any(Item.class),
                        Mockito.any(), Mockito.any()))
                .thenReturn(booking);

        service.getAll(user.getId());

        Mockito.verify(itemRepository, Mockito.times(1))
                .findAllByOwnerOrderById(user.getId());

        Mockito.verify(bookingRepository, Mockito.times(2))
                .findFirstByItemAndStartBetweenOrderByStartDesc(Mockito.any(Item.class),
                Mockito.any(), Mockito.any());

    }

    @Test
    public void addCommentTest() throws EntityNotFoundException, BadCommentException {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setId(1L);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setId(1L);
        comment.setUser(user);
        comment.setText("коммент");

        List<Item> items = new ArrayList<>();
        items.add(item);


        Mockito.when(bookingRepository.findFirstByBookerAndItemOrderByStart(user, item))
                .thenReturn(booking);

        service.addComment(user.getId(), item.getId(), comment);

        Mockito.verify(itemRepository, Mockito.times(1))
                .save(item);
        Mockito.verify(commentRepository, Mockito.times(1))
                .save(comment);

    }

    @Test
    public void addCommentByNotBookerTest() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setId(1L);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setId(1L);
        comment.setUser(user);
        comment.setText("коммент");

        List<Item> items = new ArrayList<>();
        items.add(item);

        Assertions.assertThrows(BadCommentException.class, () -> service.addComment(user.getId(), item.getId(), comment));

    }

    @Test
    public void addCommentByAPPROVEDStateTest() throws EntityNotFoundException, BadCommentException {

        Mockito.when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(item));

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setId(1L);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setStatus(State.APPROVED);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("коммент");

        List<Item> items = new ArrayList<>();
        items.add(item);

        Mockito.when(bookingRepository.findFirstByBookerAndItemOrderByStart(Mockito.any(User.class), Mockito.any(Item.class)))
                .thenReturn(booking);

        service.addComment(user.getId(), item.getId(), comment);

        Assertions.assertEquals(user, comment.getUser());

    }

    @Test
    public void searchItemTest() {
        List<Item> items = new ArrayList<>();
        items.add(item);

        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(itemDto);
        Mockito.when(itemRepository.findAllByNameOrDescriptionContainingIgnoreCase(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(items);

        service.search("описание");

        Mockito.verify(itemRepository, Mockito.times(1))
                .findAllByNameOrDescriptionContainingIgnoreCase(Mockito.anyString(), Mockito.anyString());
    }
}
