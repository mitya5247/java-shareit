package ru.practicum.shareit_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.UserClient;
import ru.practicum.user.UserControllerGateway;
import ru.practicum.user.dto.User;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerGateway.class)
public class UserTestsControllerGateway {

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mvc;
    @MockBean
    UserClient client;
    User user;

    @BeforeEach
    public void initUser() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.ru");

    }

    @Test
    public void createUserWithNEmptyEmail() throws Exception {

        user.setEmail("");

        String json = mapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithNullEmail() throws Exception {

        user.setEmail(null);

        String json = mapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithNEmptyName() throws Exception {

        user.setName("");

        String json = mapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithNullName() throws Exception {

        user.setName(null);

        String json = mapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createUserWithBadEmail() throws Exception {

        user.setEmail("bla bla bla");

        String json = mapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }
}
