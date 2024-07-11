package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.Constants;
import ru.practicum.client.BaseClient;

import java.lang.annotation.ElementType;

@Service
public class UserClient extends BaseClient {

    String uriPrefix;

    @Autowired
    public UserClient(RestTemplateBuilder restTemplateBuilder, @Value(value = "${server-uri}")
                      String uriPrefix) {
        super(restTemplateBuilder.
                uriTemplateHandler(new DefaultUriBuilderFactory(uriPrefix + Constants.PATH_USERS)).build());
        this.uriPrefix = uriPrefix;
    }

    @Override
    protected <T> ResponseEntity<Object> post(String path, T body) {
        return super.post(uriPrefix, body);
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
