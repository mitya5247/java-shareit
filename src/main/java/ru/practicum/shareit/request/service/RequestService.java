package ru.practicum.shareit.request.service;

import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.exceptions.NotEmptyDescriptionException;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    Request create(Long requestId, Request request) throws NotEmptyDescriptionException, EntityNotFoundException;

    List<Request> getRequestOfUser(Long userId) throws EntityNotFoundException;

    List<Request> getAllRequest(Long userId, Long from, Long size) throws EntityNotFoundException;

    Request getOneRequest(Long userId, Long requestId) throws EntityNotFoundException;


}
