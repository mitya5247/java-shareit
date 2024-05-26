package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    public ItemDto add(Long userId, ItemDto itemDto);
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto);
    public ItemDto get(Long id);
    public List<ItemDto> getAll(Long userId);
    public List<ItemDto> search(String word);
}
