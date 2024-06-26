package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    final UserService service;

    @PostMapping
    public User create(@Valid @RequestBody User user) throws EmailAlreadyExistsException {
        return service.create(user);
    }

    @PatchMapping(Constants.PATH_USER_ID)
    public User update(@PathVariable Long userId, @RequestBody User user) {
        return service.update(userId, user);
    }

    @DeleteMapping(Constants.PATH_USER_ID)
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }

    @GetMapping(Constants.PATH_USER_ID)
    public User get(@PathVariable Long userId) {
        return service.get(userId);
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }
}
