package ru.practicum.shareit.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonCommentTests {

    @Autowired
    JacksonTester<Comment> tester;

    User user;

    Comment comment;

    @BeforeEach
    public void loadContext() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.ru");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setCreated(LocalDateTime.now());
        comment.setUser(user);
    }

    @Test
    public void serializeCommentTest() throws IOException {

        JsonContent<Comment> content = tester.write(comment);

        assertThat(content).extractingJsonPathNumberValue("$.id", comment.getId());
        assertThat(content).extractingJsonPathStringValue("$.text", comment.getText());
        assertThat(content).extractingJsonPathValue("$.created", comment.getCreated());
        assertThat(content).extractingJsonPathValue("$.user", comment.getCreated());
    }

    @Test
    public void deSerializeCommentTest() throws IOException {

        JsonContent<Comment> content = tester.write(comment);

        ObjectContent<Comment> objectContent = tester.parse(content.getJson());

        Comment comment1 = objectContent.getObject();

        Assertions.assertEquals(comment.getId(), comment1.getId());
        Assertions.assertEquals(comment.getUser(), comment1.getUser());
        Assertions.assertEquals(comment.getCreated(), comment1.getCreated());
        Assertions.assertEquals(comment.getText(), comment1.getText());

    }
}
