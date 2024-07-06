package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exceptions.EntityNotFound;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    public User create(User user);

    public User update(Long userId, User user) throws EntityNotFound;

    public void delete(Long id) throws EntityNotFound;

    public User get(Long id) throws EntityNotFound;

    public List<User> getAll();
}
