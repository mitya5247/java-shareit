package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        return repository.add(userId, itemDto);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        return repository.update(userId, itemId, itemDto);
    }

    @Override
    public ItemDto get(Long id) {
        return repository.get(id);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return repository.getAll(userId);
    }

    @Override
    public List<ItemDto> search(String word) {
        return repository.search(word);
    }
}
