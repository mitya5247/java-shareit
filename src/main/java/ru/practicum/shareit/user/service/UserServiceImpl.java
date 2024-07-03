package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository repository;

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User update(Long userId, User user) {
        User user1 = this.userNotFound(userId);
        this.fillFields(user1, user);
        repository.save(user1);
        log.info("update user with id " + user.getId());
        return user1;
    }

    @Override
    public void delete(Long id) {
        User user = this.userNotFound(id);
        log.info("delete user with id " + id);
        repository.delete(user);
    }

    @Override
    public User get(Long id) {
        return this.userNotFound(id);
    }

    @Transactional
    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @SneakyThrows
    private User userNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFound("user with id " + id + " was not found"));
    }

    @SneakyThrows
    private void checkEmailOnDuplicate(String email) {
        for (User user : this.getAll()) {
            if (user.getEmail().equals(email)) {
                throw new EmailAlreadyExistsException("email " + email + " is already exists");
            }
        }
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
