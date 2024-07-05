package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    Request create(Long requestId, Request request);

    List<Request> getRequestOfUser(Long userId);

    List<Request> getAllRequest(Long userId, Long from, Long size);

    Request getOneRequest(Long userId, Long requestId);


}
