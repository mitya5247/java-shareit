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
import ru.practicum.Constants;
import ru.practicum.item.ItemClient;
import ru.practicum.item.ItemControllerGateway;
import ru.practicum.item.dto.Comment;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.User;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemControllerGateway.class)
public class ItemTestsControllerGateway {
    @Autowired
    MockMvc mvc;

    @MockBean
    ItemClient client;

    User user;
    ItemDto itemDto;

    ObjectMapper mapper = new ObjectMapper();

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
    }

    @Test
    public void createItemWithEmptyNameTest() throws Exception {

        itemDto.setName("");

        String json = mapper.writeValueAsString(itemDto);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createItemWithNullNameTest() throws Exception {

        itemDto.setName(null);

        String json = mapper.writeValueAsString(itemDto);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createItemWithEmptyDescriptionTest() throws Exception {

        itemDto.setDescription("");

        String json = mapper.writeValueAsString(itemDto);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createItemWithNullDescriptionTest() throws Exception {

        itemDto.setDescription(null);

        String json = mapper.writeValueAsString(itemDto);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createItemWithNullAvailableTest() throws Exception {

        itemDto.setAvailable(null);

        String json = mapper.writeValueAsString(itemDto);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createItemTest() throws Exception {

        String json = mapper.writeValueAsString(itemDto);

        ResponseEntity<Object> response = new ResponseEntity<>(itemDto, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method postName = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        postName.setAccessible(true);

        Mockito.when(postName.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(response);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void updateItemTest() throws Exception {

        itemDto.setDescription("new description");

        String json = mapper.writeValueAsString(itemDto);

        ResponseEntity<Object> response = new ResponseEntity<>(itemDto, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method patch = className.getDeclaredMethod("patch", String.class, long.class, Object.class);

        patch.setAccessible(true);

        Mockito.when(patch.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(response);

        mvc.perform(patch("/items/" + itemDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void getItemTest() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(itemDto, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class, long.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(response);

        mvc.perform(get("/items/" + itemDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void getAllItemsTest() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(itemDto, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class, long.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(response);

        mvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void getSearchItemsTest() throws Exception {

        ResponseEntity<Object> response = new ResponseEntity<>(itemDto, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method get = className.getDeclaredMethod("get", String.class);

        get.setAccessible(true);

        Mockito.when(get.invoke(client, Mockito.anyString()))
                .thenReturn(response);

        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void addCommentTest() throws Exception {

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthorName(user.getName());
        comment.setText("text");

        ResponseEntity<Object> response = new ResponseEntity<>(comment, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method post = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        post.setAccessible(true);

        Mockito.when(post.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(Comment.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(comment);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(comment.getId())))))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())))
                .andExpect(jsonPath("$.text", is(comment.getText())));
    }

    @Test
    public void addCommentWithEmptyTextTest() throws Exception {

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthorName(user.getName());
        comment.setText(" ");

        ResponseEntity<Object> response = new ResponseEntity<>(comment, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method post = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        post.setAccessible(true);

        Mockito.when(post.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(Comment.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(comment);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCommentWithNullTextTest() throws Exception {

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthorName(user.getName());
        comment.setText(null);

        ResponseEntity<Object> response = new ResponseEntity<>(comment, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method post = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        post.setAccessible(true);

        Mockito.when(post.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(Comment.class)))
                .thenReturn(response);

        String json = mapper.writeValueAsString(comment);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.HEADER, user.getId())
                        .content(json)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }
}
