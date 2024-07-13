package ru.practicum.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.user.dto.User;

import javax.validation.Valid;

@RestController
@RequestMapping(path = Constants.PATH_USERS)
public class UserControllerGateway {

    private final UserClient client;

    private final String baseUri;

    @Autowired
    public UserControllerGateway(UserClient client, @Value("${server-uri}") String baseUri) {
        this.client = client;
        this.baseUri = baseUri + Constants.PATH_USERS;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody User user) {
        return client.post(baseUri, user);
    }

    @PatchMapping(Constants.PATH_USER_ID)
    public ResponseEntity<Object> update(@PathVariable Long userId, @RequestBody User user) {
        String uri = baseUri + "/" + userId;
        return client.patch(uri, user);
    }

    @DeleteMapping(Constants.PATH_USER_ID)
    public void delete(@PathVariable Long userId) {
        String uri = baseUri + "/" + userId;
        client.delete(uri, userId);
    }

    @GetMapping(Constants.PATH_USER_ID)
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        String uri = baseUri + "/" + userId;
        return client.get(uri, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return client.get(baseUri);
    }
}
