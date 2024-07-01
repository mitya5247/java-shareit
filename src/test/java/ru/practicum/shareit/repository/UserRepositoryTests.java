package ru.practicum.shareit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;
import java.util.List;

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
        List<User> userList = repository.findAll();
        Assertions.assertEquals(user.getName() , service.get(id).getName());
        Assertions.assertEquals(user.getEmail() , service.get(id).getEmail());
    }

    @Test
    public void get() {
        List<User> userList = repository.findAll();

        Assertions.assertEquals(user.getName() , service.get(id).getName());
        Assertions.assertEquals(user.getEmail() , service.get(id).getEmail());
    }

    @Test
    public void update() {
        List<User> userList = repository.findAll();

        user.setName("update");
        service.update(user.getId(), user);
        Assertions.assertEquals(user.getName() , service.get(id).getName());
        Assertions.assertEquals(user.getEmail() , service.get(id).getEmail());
    }

    @Test
    public void delete() {
        List<User> userList = repository.findAll();

        service.delete(id);
        Assertions.assertThrows(EntityNotFound.class, () -> service.get(id));
    }

    @AfterEach
    public void deleteAll() {
        List<User> userList = repository.findAll();
        repository.deleteAll();
    }

}
