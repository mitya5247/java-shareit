package ru.practicum.shareit.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonRequestTests {
    @Autowired
    JacksonTester<Request> tester;

    User user;

    Request request;

    @BeforeEach
    public void loadContext() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.ru");

        request = new Request();
        request.setId(1L);
        request.setDescription("description");
        request.setItems(new ArrayList<>());
        request.setRequestor(user.getId());
    }

    @Test
    public void serializeRequestTest() throws IOException {

        JsonContent<Request> content = tester.write(request);

        assertThat(content).extractingJsonPathNumberValue("$.id", request.getId());
        assertThat(content).extractingJsonPathStringValue("$.name", request.getDescription());
        assertThat(content).extractingJsonPathBooleanValue("$.available", request.getRequestor());
        assertThat(content).extractingJsonPathStringValue("$.description", request.getDescription());

    }

    @Test
    public void deSerializeItemTest() throws IOException {
        JsonContent<Request> content = tester.write(request);

        String json = content.getJson();

        ObjectContent<Request> objectContent = tester.parse(json);

        Request request1 = objectContent.getObject();

        objectContent.assertThat();

        Assertions.assertEquals(request.getId(), request1.getId());
        Assertions.assertEquals(request.getDescription(), request1.getDescription());
        Assertions.assertEquals(request.getItems(), request1.getItems());
        Assertions.assertEquals(request.getRequestor(), request1.getRequestor());
    }
}
