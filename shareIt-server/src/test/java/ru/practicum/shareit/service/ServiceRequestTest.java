package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.exceptions.NotEmptyDescriptionException;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(properties = "db.name = test")
public class ServiceRequestTest {
    @MockBean
    RequestRepository requestRepository;
    @MockBean
    UserRepository userRepository;
    @Autowired
    RequestService service;
    User user;
    Request request;

    @BeforeEach
    public void initRequest() {
        user = new User();
        user.setId(1L);
        user.setEmail("email@name.ru");
        user.setName("name");

        request = new Request();
        request.setItems(new ArrayList<>());
        request.setId(1L);
        request.setRequestor(1L);
        request.setCreated(LocalDateTime.now());
        request.setDescription("описание");
    }

    @Test
    public void createRequestTest() throws EntityNotFoundException, NotEmptyDescriptionException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(requestRepository.save(Mockito.any(Request.class)))
                .thenReturn(request);

        service.create(user.getId(), request);

        Mockito.verify(requestRepository, Mockito.times(1))
                .save(Mockito.any(Request.class));

        Assertions.assertEquals(request, requestRepository.save(request));

    }

    @Test
    public void createRequestWithoutItemsTest() throws EntityNotFoundException, NotEmptyDescriptionException {

        request.setItems(null);
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(requestRepository.save(Mockito.any(Request.class)))
                .thenReturn(request);

        service.create(user.getId(), request);

        Mockito.verify(requestRepository, Mockito.times(1))
                .save(Mockito.any(Request.class));

        Assertions.assertEquals(request, requestRepository.save(request));

    }

    @Test
    public void createRequestByUnknownUserTest() {

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.create(user.getId(), request));

    }

    @Test
    public void getRequestOfUserTest() throws EntityNotFoundException {
        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        List<Request> requestList = new ArrayList<>();
        requestList.add(request);

        Mockito.when(requestRepository.findByRequestorOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(requestList);

        service.getRequestOfUser(user.getId());

        Mockito.verify(requestRepository, Mockito.times(1))
                .findByRequestorOrderByCreatedDesc(Mockito.anyLong());

        Assertions.assertEquals(requestList, requestRepository.findByRequestorOrderByCreatedDesc(user.getId()));

    }

    @Test
    public void getAllRequestsTest() throws EntityNotFoundException {

        List<Request> requestList = new ArrayList<>();
        requestList.add(request);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));


        Page<Request> page1 = Mockito.mock(Page.class);

        Mockito.when(requestRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page1);

        Mockito.when(page1.toList())
                .thenReturn(requestList);

        service.getAllRequest(user.getId(), 0L, 10L);


        Mockito.verify(requestRepository, Mockito.times(1))
                .findAll(PageRequest.of(0, 10, Sort.by("created").descending()));

        List<Request> requestList1 = requestRepository.findAll(PageRequest.of(0, 10, Sort.by("created").descending())).toList();

        Assertions.assertEquals(requestList, requestList1);

    }

    @Test
    public void getAllRequestsWithNegativeFromTest() {

        List<Request> requestList = new ArrayList<>();
        requestList.add(request);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));


        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getAllRequest(user.getId(), -1L, 10L));

    }

    @Test
    public void getAllRequestsWithNegativeSizeTest() {

        List<Request> requestList = new ArrayList<>();
        requestList.add(request);

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));


        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getAllRequest(user.getId(), 1L, -10L));

    }

    @Test
    public void getOneRequestTest() throws EntityNotFoundException {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito.when(requestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(request));

        service.getOneRequest(user.getId(), request.getId());


        Mockito.verify(requestRepository, Mockito.times(1))
                .findById(request.getId());

        Assertions.assertEquals(request, service.getOneRequest(user.getId(), request.getId()));

    }

    @Test
    public void getOneUnknownRequestTest() {

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.getOneRequest(user.getId(), 100L));

    }
}
