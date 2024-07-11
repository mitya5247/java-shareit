package ru.practicum.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.user.dto.User;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.PATH_USERS)
public class UserControllerGateway {
    private final UserClient client;

    @Autowired
    public UserControllerGateway(UserClient client) {
        this.client = client;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {
        return client.post("http://localhost:8081/users", user);
    }

    @PatchMapping(Constants.PATH_USER_ID)
    public ResponseEntity<Object> update(@PathVariable Long userId, @RequestBody User user) throws EntityNotFoundException {
        return client.patch(Constants.PATH_USERS, userId, user);
    }

    @DeleteMapping(Constants.PATH_USER_ID)
    public void delete(@PathVariable Long userId) throws EntityNotFoundException {
        client.delete(Constants.PATH_USERS, userId);
    }

    @GetMapping(Constants.PATH_USER_ID)
    public ResponseEntity<Object> get(@PathVariable Long userId) throws EntityNotFoundException {
        return client.get(Constants.PATH_USERS, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return client.get(Constants.PATH_USERS);
    }
}
