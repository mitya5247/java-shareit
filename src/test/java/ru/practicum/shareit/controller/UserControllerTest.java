package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    ObjectMapper mapper = new ObjectMapper();

    User user;

    @BeforeEach
    public void initUser() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.ru");
    }

    @Test
    public void createUser() throws Exception {

        Mockito.when(service.create(Mockito.any(User.class)))
                .thenReturn(user);


        String json = mapper.writeValueAsString(user);

        mvc.perform(post("/users")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(user.getId())))))
            .andExpect(jsonPath("$.email", is(user.getEmail())))
            .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    public void updateUserTest() throws Exception {
        user.setName("update");

        Mockito.when(service.update(Mockito.anyLong(), Mockito.any(User.class)))
                .thenReturn(user);

        String json = mapper.writeValueAsString(user);


        mvc.perform(patch("/users/" + user.getId().toString())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(user.getId())))))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    public void deleteUserTest() throws Exception {
        user.setName("update");


        mvc.perform(delete("/users/" + user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserTest() throws Exception {

        Mockito.when(service.get(Mockito.anyLong()))
                .thenReturn(user);

        mvc.perform(get("/users/" + user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(user.getId())))))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    public void getAllUsersTest() throws Exception {

        List<User> users = new ArrayList<>();
        users.add(user);

        Mockito.when(service.getAll())
                .thenReturn(users);

        mvc.perform(get("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(String.valueOf(user.getId())))))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].name", is(user.getName())));
    }
}
