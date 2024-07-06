package ru.practicum.shareit.request.service;

import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.exceptions.NotEmptyDescription;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    Request create(Long requestId, Request request) throws NotEmptyDescription, EntityNotFound;

    List<Request> getRequestOfUser(Long userId) throws EntityNotFound;

    List<Request> getAllRequest(Long userId, Long from, Long size) throws EntityNotFound;

    Request getOneRequest(Long userId, Long requestId) throws EntityNotFound;


}
