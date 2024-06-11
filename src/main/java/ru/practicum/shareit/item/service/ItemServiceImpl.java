package ru.practicum.shareit.item.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemRepository repository;

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        this.checkExistUser(userId);
        Item item = Mapper.convertToItem(userId, itemDto);
        repository.save(item);
        return itemDto;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        this.checkExistUser(userId);
        this.itemNotFound(itemId);
        Item item = Mapper.convertToItem(userId, itemDto);
        repository.save(item);
        return itemDto;
    }

    @Override
    public ItemDto get(Long id) {
        Item item = repository.findById(id).get();
        return Mapper.convertToDto(item);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<Item> items = repository.findAllByOwner(userId);
        List<ItemDto> itemDtos = items.stream()
                .map(Mapper::convertToDto)
                .collect(Collectors.toList());
        return itemDtos;
    }

    @Override
    public List<ItemDto> search(String word) {
        List<Item> items = repository.findAllByNameOrDescriptionIgnoreCase(word, word);
        List<ItemDto> itemDtos = items.stream()
                .map(Mapper::convertToDto)
                .collect(Collectors.toList());
        return itemDtos;
    }

    @SneakyThrows
    private void checkExistUser(Long id) {
        repository.findAll().stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("user c id " + id + " не сущуствует"));

    }

    @SneakyThrows
    private Item itemNotFound(Long itemId) {
        return repository.findAll().stream()
                .filter(item -> Objects.equals(item.getId(), itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("item c id " + itemId + " не найден"));
    }

}
