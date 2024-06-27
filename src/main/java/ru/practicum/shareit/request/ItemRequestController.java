package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.handler.ErrorResponse;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

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
    public Request create(@RequestHeader(Constants.HEADER) Long userId, @RequestBody Request request) {
        return service.create(userId, request);
    }

    @GetMapping
    public List<Request> getRequest(@RequestHeader(Constants.HEADER) Long userId) {
        return service.getRequestOfUser(userId);
    }

    @GetMapping("/all")
    public List<Request> getAllRequests(@RequestHeader(Constants.HEADER) Long userId, @RequestParam(required = false) Long from,
                                        @RequestParam(required = false) Long size) {
        return service.getAllRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Request getOneRequest(@RequestHeader(Constants.HEADER) Long userId, @PathVariable Long requestId) {
        return service.getOneRequest(userId, requestId);
    }

}
