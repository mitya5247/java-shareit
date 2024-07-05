package ru.practicum.shareit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;

@SpringBootTest(properties = "db.name = test")
public class UserRepositoryTests {
    @Autowired
    EntityManager em;
    @Autowired
    UserRepository repository;
    @Autowired
    UserService service;
    User user;
    Long id;

    @BeforeEach
    @Transactional
    public void initUser() {
        user = new User();
        user.setEmail("email@name.ru");
        user.setName("name");

        id = service.create(user).getId();
    }

    @Test
    public void create() {
        Assertions.assertEquals(user.getName(), service.get(id).getName());
        Assertions.assertEquals(user.getEmail(), service.get(id).getEmail());
    }

    @Test
    public void get() {

        Assertions.assertEquals(user.getName(), service.get(id).getName());
        Assertions.assertEquals(user.getEmail(), service.get(id).getEmail());
    }

    @Test
    public void update() {
        user.setName("update");
        service.update(user.getId(), user);
        Assertions.assertEquals(user.getName(), service.get(id).getName());
        Assertions.assertEquals(user.getEmail(), service.get(id).getEmail());
    }

    @Test
    public void delete() {
        service.delete(id);
        Assertions.assertThrows(EntityNotFound.class, () -> service.get(id));
    }

    @AfterEach
    public void deleteAll() {
        repository.deleteAll();
    }

}
