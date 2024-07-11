package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory())
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    @Override
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return super.post(path, body);
    }

    @Override
    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return super.patch(path, body);
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
