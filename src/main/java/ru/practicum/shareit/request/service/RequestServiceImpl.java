package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.exceptions.NotEmptyDescription;

import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    UserRepository userRepository;
    RequestRepository requestRepository;


    @SneakyThrows
    @Override
    public Request create(Long userId, Request request) {
        User user = this.userNotFound(userId);
        this.isEmptyDescription(request);
        request.setRequestor(userId);
        if (request.getItems() == null) {
            request.setItems(new ArrayList<>());
        }
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getRequestOfUser(Long userId) {
        User user = this.userNotFound(userId);
        List<Request> requests = requestRepository.findByRequestorOrderByCreatedDesc(userId);
        for (Request request : requests) {
            this.fillItemsRequestDto(request, userId);
        }
        return requests;
    }

    @Override
    public List<Request> getAllRequest(Long userId, Long from, Long size) {
        User user = this.userNotFound(userId);
        if (from < 0) {
            throw new IllegalArgumentException("from couldn't be less 0 " + from);
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size couldn't be less 0 " + from);
        }
        int sizeInt = Integer.parseInt(String.valueOf(size));
        int fromInt = Integer.parseInt(String.valueOf(from));
        Pageable page = PageRequest.of(fromInt, sizeInt, Sort.by("created").descending());
        List<Request> requests = requestRepository.findAll(page).toList().stream()
                .filter(request -> !request.getRequestor().equals(userId))
                .map(request -> this.fillItemsRequestDto(request, userId))
                .collect(Collectors.toList());
        return requests;
    }

    @Override
    public Request getOneRequest(Long userId, Long requestId) {
        User user = this.userNotFound(userId);
        Request request = this.requestNotFound(requestId);
        this.fillItemsRequestDto(request, userId);
        return request;
    }

    @SneakyThrows
    private User userNotFound(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFound("user with id " + userId + " was not found"));
    }

    @SneakyThrows
    private Request requestNotFound(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() ->
                new EntityNotFound("request with id " + requestId + " was not found"));
    }

    private Request fillItemsRequestDto(Request request, Long userId) {
        request.setItems(requestRepository.findItems(request.getId()));
        return request;
    }

    @SneakyThrows
    private void isEmptyDescription(Request request) {
        if (request.getDescription() == null || request.getDescription().isEmpty()) {
            throw new NotEmptyDescription("descriptiom mustn't be null");
        }
    }
}
