package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exceptions.BadCommentException;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    public ItemDto add(Long userId, ItemDto itemDto) throws EntityNotFoundException;

    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) throws EntityNotFoundException;

    public ItemDto get(Long userId, Long id) throws EntityNotFoundException;

    public List<ItemDto> getAll(Long userId);

    public List<ItemDto> search(String word);

    public CommentDto addComment(Long userId, Long itemId, Comment comment) throws EntityNotFoundException, BadCommentException;

}
