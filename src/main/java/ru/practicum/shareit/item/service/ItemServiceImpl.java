package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.BadComment;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    RequestRepository requestRepository;

    @SneakyThrows
    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        this.checkExistUser(userId);
        List<Comment> comments = new ArrayList<>();
        List<CommentDto> commentDtos = new ArrayList<>();
        itemDto.setComments(commentDtos);
        Item item = Mapper.convertToItem(userId, itemDto);
        if (itemDto.getRequestId() != null) {
            Request request = requestRepository.findById(itemDto.getRequestId()).orElseThrow(() -> new
                    EntityNotFound("request with id " + itemDto.getRequestId() + "was not found"));
            item.setRequest(request);
        }
        item.setComments(comments);
        item = repository.save(item);
        return Mapper.convertToDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        this.checkExistUser(userId);
        Item item = this.itemNotFound(itemId);
        this.fillFields(userId, item, itemDto);
        repository.save(item);
        return Mapper.convertToDto(item);
    }

    @Override
    public ItemDto get(Long userId, Long id) {
        Item item = this.itemNotFound(id);
        this.synchronizeBooking(item);
        ItemDto itemDto = Mapper.convertToDto(item);
        if (!userId.equals(item.getOwner())) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<Item> items = repository.findAllByOwnerOrderById(userId);
        List<ItemDto> itemDtos = items.stream()
                .map(this::synchronizeBooking)
                .map(Mapper::convertToDto)
                .collect(Collectors.toList());
        return itemDtos;
    }

    @Override
    public List<ItemDto> search(String word) {
        List<ItemDto> itemDto = new ArrayList<>();
        if (!word.isEmpty()) {
            List<Item> items = repository.findAllByNameOrDescriptionContainingIgnoreCase(word, word);
            itemDto = items.stream()
                    .filter(item -> item.isAvailable())
                    .map(item -> Mapper.convertToDto(item))
                    .collect(Collectors.toList());
        }
        return itemDto;
    }

    @SneakyThrows
    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment comment) {
        Item item = itemNotFound(itemId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFound("user with id " + userId + " was not found"));
        Booking booking = bookingRepository.findFirstByBookerAndItemOrderByStart(user, item);
        LocalDateTime now = LocalDateTime.now();
        if (booking == null) {
            throw new BadComment("couldn't give feedback as you didn't take item");
        }
        if (booking.getStart().isAfter(now.plusSeconds(1))) {
            throw new BadComment("couldn't give feedback on the future booking");
        }
        if (booking.getStatus().equals(State.APPROVED)) {
            comment.setUser(user);
            comment.setCreated(LocalDateTime.now());
        }
        item.getComments().add(commentRepository.save(comment));
        repository.save(item);
        return Mapper.convertCommentToDto(comment);
    }

    @SneakyThrows
    private void checkExistUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFound("user with id " + id + " was not found"));
    }

    @SneakyThrows
    private Item itemNotFound(Long itemId) {
        return repository.findById(itemId).orElseThrow(() ->
                new EntityNotFound("item with id " + itemId + " was not found"));
    }

    @SneakyThrows
    private void fillFields(Long userId, Item item, ItemDto itemDto) {
        if (Objects.equals(item.getOwner(), userId)) {
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
        } else {
            throw new EntityNotFound("user with id " + userId + " is not corresponded with owner of item id " +
                    item.getId());
        }
    }

    private Item synchronizeBooking(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Booking nextBooking = bookingRepository.findFirstByItemAndStartBetweenOrderByStartDesc(item,
                now, now.plusHours(12));
        if (nextBooking != null) {
            Booking lastBooking = bookingRepository.findFirstByItemAndStartBetweenOrderByStartDesc(item,
                    now.minusHours(12), nextBooking.getStart().minusSeconds(1));
            item.setLastBooking(lastBooking);
        } else {
            Booking lastBooking = bookingRepository.findFirstByItemAndStartBetweenOrderByStartDesc(item,
                    now.minusHours(12), LocalDateTime.now());
            item.setLastBooking(lastBooking);
        }
        item.setNextBooking(nextBooking);

        return item;
    }

}
