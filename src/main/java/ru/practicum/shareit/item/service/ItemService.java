package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    public ItemDto add(Long userId, ItemDto itemDto);

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    public ItemDto get(Long userId, Long id);

    public List<ItemDto> getAll(Long userId);

    public List<ItemDto> search(String word);

    public CommentDto addComment(Long userId, Long itemId, Comment comment);

}
