package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    public User create(User user);

    public User update(Long userId, User user) throws EntityNotFoundException;

    public void delete(Long id) throws EntityNotFoundException;

    public User get(Long id) throws EntityNotFoundException;

    public List<User> getAll();
}
