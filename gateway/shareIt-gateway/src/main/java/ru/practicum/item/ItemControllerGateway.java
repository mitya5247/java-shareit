package ru.practicum.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.item.dto.Comment;
import ru.practicum.item.dto.ItemDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.PATH_ITEMS)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemControllerGateway {

    @Autowired
    ItemClient client;

    private String baseUri;

    public ItemControllerGateway(ItemClient client, @Value("${server-uri}") String serverUri) {
        this.client = client;
        baseUri = serverUri + Constants.PATH_ITEMS;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(Constants.HEADER) Long userId, @Valid @RequestBody ItemDto itemDto) {
        return client.post(baseUri, userId, itemDto);
    }

    @PatchMapping(Constants.PATH_ITEM_ID)
    public ResponseEntity<Object> update(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long itemId,
                                         @RequestBody ItemDto itemDto) {
        String uri = baseUri + "/" + itemId;
        return client.patch(uri, userId, itemDto);
    }

    @GetMapping(Constants.PATH_ITEM_ID)
    public ResponseEntity<Object> get(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long itemId) {
        String uri = baseUri + "/" + itemId;
        return client.get(uri, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(Constants.HEADER) Long userId) {
        return client.get(baseUri, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text", required = false) String word) {
        String uri = baseUri + "/search?text=" + word;
        return client.get(uri);
    }

    @PostMapping(value = Constants.PATH_ITEM_ID + "/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long itemId,
                                 @Valid @RequestBody Comment comment) {
        String uri = baseUri + "/comment" + itemId;
        return client.post(uri, userId, comment);
    }
}
