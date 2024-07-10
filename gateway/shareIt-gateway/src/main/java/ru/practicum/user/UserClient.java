package ru.practicum.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.client.BaseClient;

public class UserClient extends BaseClient {
    public UserClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return super.post(path, body);
    }

    @Override
    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return super.patch(path, userId, body);
    }

    @Override
    protected ResponseEntity<Object> delete(String path, long userId) {
        return super.delete(path, userId);
    }

    @Override
    protected ResponseEntity<Object> get(String path, long userId) {
        return super.get(path, userId);
    }

    @Override
    protected ResponseEntity<Object> get(String path) {
        return super.get(path);
    }
}
