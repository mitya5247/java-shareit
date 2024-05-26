package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    @Override
    public User create(User user) throws EmailAlreadyExistsException {
        return repository.create(user);
    }

    @Override
    public User update(Long userId, User user) {
        return repository.update(userId, user);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public User get(Long id) {
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }
}
