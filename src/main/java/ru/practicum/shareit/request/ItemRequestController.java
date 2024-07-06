package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.exceptions.NotEmptyDescription;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    @Autowired
    RequestService service;

    @PostMapping
    public Request create(@RequestHeader(Constants.HEADER) Long userId, @Valid @RequestBody Request request) throws EntityNotFound, NotEmptyDescription {
        return service.create(userId, request);
    }

    @GetMapping
    public List<Request> getRequest(@RequestHeader(Constants.HEADER) Long userId) throws EntityNotFound {
        return service.getRequestOfUser(userId);
    }

    @GetMapping("/all")
    public List<Request> getAllRequests(@RequestHeader(Constants.HEADER) Long userId, @RequestParam(required = false,
            defaultValue = "0") Long from, @RequestParam(required = false, defaultValue = "10") Long size) throws EntityNotFound {
        return service.getAllRequest(userId, from, size);
    }

    @GetMapping(Constants.PATH_REQUEST_ID)
    public Request getOneRequest(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long requestId) throws EntityNotFound {
        return service.getOneRequest(userId, requestId);
    }

}
