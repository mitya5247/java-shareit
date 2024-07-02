package ru.practicum.shareit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest(properties = "db.name = test")
public class RequestRepositoryTests {
    @Autowired
    EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    RequestRepository requestRepository;
    User user;
    Item item;
    Request request;
    Long userId;
    Long itemId;
    Long requestId;

    @BeforeEach
    public void initContext() {
        user = new User();
        user.setEmail("email@name.ru");
        user.setName("name");

        userId = userRepository.save(user).getId();

        item = new Item();
        item.setAvailable(true);
        item.setName("название");
        item.setDescription("описание");
        item.setComments(new ArrayList<>());
        item.setComments(new ArrayList<>());

        itemId = itemRepository.save(item).getId();

        request = new Request();
        request.setItems(new ArrayList<>());
        request.setDescription("описание");
        request.setRequestor(userId);
        request.setCreated(LocalDateTime.now());

        requestId = requestRepository.save(request).getId();
    }

    @Test
    public void saveRequestTest() {

        TypedQuery<Request> query = em.createQuery("select r from Request r where r.id = :id", Request.class)
                        .setParameter("id", requestId);
        Request request1 = query.getSingleResult();

        Assertions.assertEquals(requestId, request1.getId());
        Assertions.assertEquals(request.getDescription(), request1.getDescription());
        Assertions.assertEquals(request.getItems(), request1.getItems());
        Assertions.assertEquals(request.getRequestor(), request1.getRequestor());

    }

    @Test
    public void findByRequestorOrderByCreatedDescTest() {


        List<Request> requestList = requestRepository.findByRequestorOrderByCreatedDesc(userId);

        Assertions.assertEquals(requestId, requestList.get(0).getId());
        Assertions.assertEquals(request.getDescription(), requestList.get(0).getDescription());
        Assertions.assertEquals(request.getItems(), requestList.get(0).getItems());
        Assertions.assertEquals(request.getRequestor(), requestList.get(0).getRequestor());

    }
    @Test
    public void getRequestTest() {

        Request request1 = requestRepository.findById(requestId).get();

        Assertions.assertEquals(requestId, request1.getId());
        Assertions.assertEquals(request.getDescription(), request1.getDescription());
        Assertions.assertEquals(request.getItems(), request1.getItems());
        Assertions.assertEquals(request.getRequestor(), request1.getRequestor());
    }

    @Test
    public void updateRequestTest() {

        request.setDescription("новое описание");
        requestId = requestRepository.save(request).getId();
        Request request1 = requestRepository.findById(requestId).get();

        Assertions.assertEquals(requestId, request1.getId());
        Assertions.assertEquals(request.getDescription(), request1.getDescription());
        Assertions.assertEquals(request.getItems(), request1.getItems());
        Assertions.assertEquals(request.getRequestor(), request1.getRequestor());
    }

    @AfterEach
    public void deleteAll() {
        requestRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }


}
