package ru.practicum.shareit_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.practicum.Constants;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.request.ItemRequestControllerGateway;
import ru.practicum.request.RequestClient;
import ru.practicum.request.dto.Request;
import ru.practicum.user.dto.User;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestControllerGateway.class)
public class RequestTestsControllerGateway {
    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    RequestClient client;

    @Autowired
    MockMvc mvc;

    User user;
    ItemDto itemDto;
    Request request;

    @BeforeEach
    public void initContext() {
        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.ru");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);
        itemDto.setComments(new ArrayList<>());

        request = new Request();
        request.setId(1L);
        request.setDescription("description");
        request.setRequestor(user.getId());

        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void createRequest() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(request, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method post = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        post.setAccessible(true);

        Mockito.when(post.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(Request.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(request);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor", is(Integer.parseInt(String.valueOf(request.getId())))));
    }

    @Test
    public void createRequestWithEmptySescriptionTest() throws Exception {

        request.setDescription("");
        ResponseEntity<Object> response = new ResponseEntity<>(request, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method post = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        post.setAccessible(true);

        Mockito.when(post.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(Request.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(request);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createRequestWithNullSescriptionTest() throws Exception {

        request.setDescription(null);
        ResponseEntity<Object> response = new ResponseEntity<>(request, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method post = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        post.setAccessible(true);

        Mockito.when(post.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(Request.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(request);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRequest() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(request, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class, long.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER, user.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor", is(Integer.parseInt(String.valueOf(request.getId())))));
    }

    @Test
    public void getAllRequests() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(request, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class, long.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER, user.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor", is(Integer.parseInt(String.valueOf(request.getId())))));
    }

    @Test
    public void getOneRequest() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(request, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class, long.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(response);

        mvc.perform(get("/requests/" + request.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER, user.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor", is(Integer.parseInt(String.valueOf(request.getId())))));
    }

    @Test
    public void createRequestWithNegativerequestorDescriptionTest() throws Exception {

        request.setRequestor(-1L);


        String json = mapper.writeValueAsString(request);

        mvc.perform(post("/requests")
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
