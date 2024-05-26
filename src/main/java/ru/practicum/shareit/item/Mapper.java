package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class Mapper {
    public static ItemDto convertToDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
        return itemDto;
    }

    public static Item convertToItem(Long userId, ItemDto itemDto) {
        Item item = Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .owner(userId)
                .available(itemDto.getAvailable())
                .build();
        return item;
    }
}
