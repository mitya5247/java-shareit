package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exceptions.BadComment;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {

    ItemService service;

    @PostMapping
    public ItemDto add(@RequestHeader(Constants.HEADER) Long userId, @Valid @RequestBody ItemDto itemDto) throws EntityNotFound {
        return service.add(userId, itemDto);
    }

    @PatchMapping(Constants.PATH_ITEM_ID)
    public ItemDto update(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) throws EntityNotFound {
        return service.update(userId, itemId, itemDto);
    }

    @GetMapping(Constants.PATH_ITEM_ID)
    public ItemDto get(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long itemId) throws EntityNotFound {
        return service.get(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(Constants.HEADER) Long userId) {
        return service.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam(name = "text", required = false) String word) {
        return service.search(word);
    }

    @PostMapping(value = Constants.PATH_ITEM_ID + "/comment")
    public CommentDto addComment(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long itemId,
                                 @Valid @RequestBody Comment comment) throws EntityNotFound, BadComment {
        return service.addComment(userId, itemId, comment);
    }
}
