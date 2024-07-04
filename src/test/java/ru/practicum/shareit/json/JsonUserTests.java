package ru.practicum.shareit.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import ru.practicum.shareit.user.User;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonUserTests {

    @Autowired
    JacksonTester<User> tester;

    User user;

    @BeforeEach
    public void loadContext() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.ru");
    }

    @Test
    public void serializeUserTest() throws IOException {
        JsonContent<User> content = tester.write(user);

        assertThat(content).extractingJsonPathNumberValue("$.id", user.getId());
        assertThat(content).extractingJsonPathStringValue("$.email", user.getEmail());
        assertThat(content).extractingJsonPathStringValue("$.name", user.getName());
    }

    @Test
    public void deSerializeUserTest() throws IOException {
        JsonContent<User> content = tester.write(user);

        String json = content.getJson();

        ObjectContent<User> objectContent = tester.parse(json);

        User user1 = objectContent.getObject();

        objectContent.assertThat();

        Assertions.assertEquals(user.getId(), user1.getId());
        Assertions.assertEquals(user.getEmail(), user1.getEmail());
        Assertions.assertEquals(user.getName(), user1.getName());

    }

}
