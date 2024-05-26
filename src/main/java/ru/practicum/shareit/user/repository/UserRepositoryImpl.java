package ru.practicum.shareit.user.repository;


import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Data
public class UserRepositoryImpl implements UserRepository {

    List<User> users = new ArrayList<>();
    long idGen = 1;

    @Override
    public User create(User user) {
        this.checkEmailOnDuplicate(user.getEmail());
        user.setId(idGen);
        idGen++;
        users.add(user);
        return user;

    }

    @Override
    public User update(Long userId, User userNew) {
        User user = this.userNotFound(userId);
        if (!user.getEmail().equals(userNew.getEmail())) {
            this.checkEmailOnDuplicate(userNew.getEmail());
        }
        this.fillFields(user, userNew);
        return user;
    }

    @Override
    public void delete(Long id) {
        this.userNotFound(id);
        users.removeIf(user -> user.getId() == id);
    }

    @Override
    public User get(Long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @SneakyThrows
    private void checkEmailOnDuplicate(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                throw new EmailAlreadyExistsException("email " + email + " уже существует");
            }
        }
    }

    @SneakyThrows
    private User userNotFound(Long id) {
        for (User user : users) {
            if (Objects.equals(user.getId(), id)) {
                return user;
            }
        }
        throw new EntityNotFoundException("user c id " + id + " не найден");
    }

    private void fillFields(User user, User userNew) {
        if (userNew.getEmail() != null) {
            user.setEmail(userNew.getEmail());
        }
        if (userNew.getName() != null) {
            user.setName(userNew.getName());
        }
    }
}
