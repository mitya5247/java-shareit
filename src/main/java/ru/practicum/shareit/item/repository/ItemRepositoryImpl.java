package ru.practicum.shareit.item.repository;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRepositoryImpl implements ItemRepository {

    private final UserRepository repository;
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
        return itemDto;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        this.checkExistUser(userId);
        Item item = this.itemNotFound(itemId);
        this.fillFields(userId, item, itemDto);
        ItemDto itemDtoNew = Mapper.convertToDto(item);
        return itemDtoNew;
    }

    @Override
    public ItemDto get(Long id) {
        for (Item item : items) {
            if (item.getId() == id) {
                ItemDto itemDto = Mapper.convertToDto(item);
                return itemDto;
            }
        }
        return null;
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            if (item.getOwner() == userId) {
                ItemDto itemDto = Mapper.convertToDto(item);
                itemsDto.add(itemDto);
            }
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> search(String word) {
        List<ItemDto> listBySearch = new ArrayList<>();
        if (!word.isBlank()) {
            String wordLow = word.toLowerCase();
            for (Item item : items) {
                String lowName = item.getName().toLowerCase();
                String lowDesc = item.getDescription().toLowerCase();
                if (lowName.contains(wordLow) || lowDesc.contains(wordLow) && item.isAvailable()) {
                    ItemDto itemDto = Mapper.convertToDto(item);
                    listBySearch.add(itemDto);
                }
            }
        }
        return listBySearch;
    }

    @SneakyThrows
    private Item itemNotFound(Long itemId) {
        for (Item item : items) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        throw new EntityNotFoundException("item c id " + itemId + " не найден");
    }

    @SneakyThrows
    private void fillFields(Long userId, Item item, ItemDto itemDto) {
        if (item.getOwner() == userId) {
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
        for (User user : repository.getAll()) {
            if (user.getId() == id) {
                return;
            }
        }
        throw new EntityNotFoundException("user c id " + id + " не сущуствует");
    }
}
