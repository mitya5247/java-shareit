package ru.practicum.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.exceptions.NotEmptyDescriptionException;
import ru.practicum.request.dto.Request;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemRequestControllerGateway {

    @Autowired
    RequestClient client;

    String baseUri;

    public ItemRequestControllerGateway(RequestClient client,@Value("${server-uri}") String baseUri) {
        this.client = client;
        this.baseUri = baseUri + Constants.PATH_REQUESTS;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Constants.HEADER) Long userId, @Valid @RequestBody Request request)
            throws NotEmptyDescriptionException {
        return client.post(baseUri, userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getRequest(@RequestHeader(Constants.HEADER) Long userId) {
        return client.get(baseUri, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(Constants.HEADER) Long userId, @RequestParam(required = false,
            defaultValue = "0") Long from, @RequestParam(required = false, defaultValue = "10") Long size) {
        String uri = baseUri + "/all" + "?from=" + from + "&size=" + size;
        return client.get(uri, userId);
    }

    @GetMapping(Constants.PATH_REQUEST_ID)
    public ResponseEntity<Object> getOneRequest(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long requestId) {
        String uri = baseUri + "/" + requestId;
        return client.get(uri, userId);
    }
}
