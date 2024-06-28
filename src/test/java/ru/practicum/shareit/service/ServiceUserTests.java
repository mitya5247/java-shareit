package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Optional;

@SpringBootTest(properties = "db.name=test")
public class ServiceUserTests {
    @Autowired
    UserService service;
    @MockBean
    UserRepository repository;
    User user;

    @BeforeEach
    public void createUser() {
        user = new User();
        user.setId(1L);
        user.setEmail("email@name.ru");
        user.setName("name");
    }

    @Test
    public void testCreationUser() {
        service.create(user);

        Mockito.when(repository.save(user))
                        .thenReturn(user);

        Mockito.verify(repository, Mockito.times(1))
                .save(user);

        Assertions.assertEquals(repository.save(user), user);
    }

    @Test
    public void testUpdateUser() {

        user.setName("updateName");

        Mockito.when(repository.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));
        service.update(user.getId(), user);

        Mockito.verify(repository, Mockito.times(1))
                .save(user);

        Assertions.assertEquals(service.update(user.getId(), user), user);
    }

    @Test
    public void testDeleteUser() {
        Mockito.when(repository.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));
        service.delete(user.getId());

        Mockito.verify(repository, Mockito.times(1))
                .delete(user);
    }

    @Test
    public void testGetUser() {
        Mockito.when(repository.findById(user.getId()))
                .thenReturn(Optional.ofNullable(user));
        service.get(user.getId());

        Mockito.verify(repository, Mockito.times(1))
                .findById(user.getId());
        Assertions.assertEquals(service.get(user.getId()), user);
    }

    @Test
    public void testGetAllUsers() {
        service.getAll();
        Mockito.verify(repository, Mockito.times(1))
                .findAll();
    }
}
