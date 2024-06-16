package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadComment;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
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

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        this.checkExistUser(userId);
        List<Comment> comments = new ArrayList<>();
        List<CommentDto> commentDtos = new ArrayList<>();
        itemDto.setComments(commentDtos);
        Item item = Mapper.convertToItem(userId, itemDto);
        item.setComments(comments);
        return Mapper.convertToDto(repository.save(item));
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
 //       if (item.getNextBooking() != null && item.getNextBooking().getEnd().isBefore(LocalDateTime.now())) {
 //           item.setLastBooking(item.getNextBooking());
 //       }
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
                () -> new EntityNotFoundException("user c id " + userId + " не сущуствует"));
        if (item.getLastBooking().getStart().isAfter(LocalDateTime.now())) {
            throw new BadComment("нельзя добавить комментарий о бронировании в будущем");
        }
        if (item.getLastBooking().getBooker().getId().equals(userId)) {
            comment.setUser(user);
            comment.setCreated(LocalDateTime.now());
        }
        item.getComments().add(commentRepository.save(comment));
        repository.save(item);
        return Mapper.convertCommentToDto(comment);
    }

    @SneakyThrows
    private void checkExistUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user c id " + id + " не сущуствует"));
      //  userRepository.findAll().stream()
      //          .filter(user -> Objects.equals(user.getId(), id))
      //          .findFirst()
       //         .orElseThrow(() -> new EntityNotFoundException("user c id " + id + " не сущуствует"));
    }

    @SneakyThrows
    private Item itemNotFound(Long itemId) {
        return repository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("item c id " + itemId + " не найден"));
     //   return repository.findAll().stream()
       //         .filter(item -> Objects.equals(item.getId(), itemId))
       //         .findFirst()
      //          .orElseThrow(() -> new EntityNotFoundException("item c id " + itemId + " не найден"));
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
            throw new EntityNotFoundException("user c id " + userId + " не совпадает с создателем item id " +
                    item.getId());
        }
    }

}
