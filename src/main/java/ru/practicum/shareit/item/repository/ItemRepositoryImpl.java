package ru.practicum.shareit.item.repository;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {

    UserRepository repository;
    List<Item> items = new ArrayList<>();
    long idGen = 1;

    @Autowired
    public ItemRepositoryImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        this.checkExistUser(userId);
        itemDto.setId(idGen);
        idGen++;
        Item item = Mapper.convertToItem(userId, itemDto);
        items.add(item);
        log.info("добавлен элемент с id " + item.getId() + " пользователем с id " + userId);
        return itemDto;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        this.checkExistUser(userId);
        Item item = this.itemNotFound(itemId);
        this.fillFields(userId, item, itemDto);
        ItemDto itemDtoNew = Mapper.convertToDto(item);
        log.info("обновлен элемент с id " + item.getId() + " пользователем с id " + userId);
        return itemDtoNew;
    }

    @Override
    public ItemDto get(Long id) {
        return items.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .map(Mapper::convertToDto)
                .orElse(null);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return items.stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .map(Mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String word) {
        if (!word.isBlank()) {
            return items.stream()
                    .filter(item -> {
                                String wordLow = word.toLowerCase();
                                return item.getName().toLowerCase().contains(wordLow) ||
                                        item.getDescription().toLowerCase().contains(wordLow)
                                                && item.isAvailable();
                            }
                    )
                    .map(Mapper::convertToDto)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @SneakyThrows
    private Item itemNotFound(Long itemId) {
        return items.stream()
                .filter(item -> Objects.equals(item.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("item c id " + itemId + " не найден"));
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

    @SneakyThrows
    private void checkExistUser(Long id) {
        this.repository.getAll().stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("user c id " + id + " не сущуствует"));

    }
}
