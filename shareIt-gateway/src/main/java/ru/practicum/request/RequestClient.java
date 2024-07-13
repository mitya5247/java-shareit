package ru.practicum.request;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

@Service
public class RequestClient extends BaseClient {

    public RequestClient(RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory())
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    @Override
    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return super.post(path, userId, body);
    }

    @Override
    protected <T> ResponseEntity<Object> patch(String path, long userId) {
        return super.patch(path, userId);
    }

    @Override
    protected ResponseEntity<Object> get(String path, long userId) {
        return super.get(path, userId);
    }
}
