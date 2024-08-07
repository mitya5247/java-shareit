package ru.practicum.shareit.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
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
    public User update(Long userId, User user) throws EntityNotFoundException {
        User user1 = this.userNotFound(userId);
        this.fillFields(user1, user);
        repository.save(user1);
        log.info("update user with id " + user.getId());
        return user1;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        User user = this.userNotFound(id);
        log.info("delete user with id " + id);
        repository.delete(user);
    }

    @Override
    public User get(Long id) throws EntityNotFoundException {
        return this.userNotFound(id);
    }

    @Transactional
    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    private User userNotFound(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("user with id " + id + " was not found"));
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
