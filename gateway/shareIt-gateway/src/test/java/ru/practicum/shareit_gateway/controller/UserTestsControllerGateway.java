package ru.practicum.shareit_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.user.UserClient;
import ru.practicum.user.UserControllerGateway;
import ru.practicum.user.dto.User;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    public void createUser() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method post = className.getDeclaredMethod("post", String.class, Object.class);

        post.setAccessible(true);

        Mockito.when(post.invoke(client, Mockito.anyString(), Mockito.any(User.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(user);

        mvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(user.getId())))))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

    }

    @Test
    public void updateUser() throws Exception {

        user.setName("new_name");

        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method patch = className.getDeclaredMethod("patch", String.class, Object.class);

        patch.setAccessible(true);

        Mockito.when(patch.invoke(client, Mockito.anyString(), Mockito.any(User.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(user);

        mvc.perform(patch("/users/" + user.getId())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(user.getId())))))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

    }

    @Test
    public void getUser() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class, long.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(response);

        mvc.perform(get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(user.getId())))))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

    }

    @Test
    public void getAllUsers() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString()))
                .thenReturn(response);

        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(user.getId())))))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

    }

    @Test
    public void deleteUser() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(user, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method delete = className.getDeclaredMethod("delete", String.class, long.class);

        delete.setAccessible(true);

        Mockito.when(delete.invoke(client, Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(response);

        mvc.perform(delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

    }
}
