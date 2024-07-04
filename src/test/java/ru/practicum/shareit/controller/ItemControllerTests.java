package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    ItemService itemService;

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
    public void createItemTest() throws Exception {

        Mockito.when(itemService.add(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDto);

        String json = mapper.writeValueAsString(itemDto);

        mvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .header(Constants.HEADER, user.getId())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void updateItemTest() throws Exception {

        itemDto.setDescription("updatedDescription");

        Mockito.when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDto);

        String json = mapper.writeValueAsString(itemDto);

        mvc.perform(patch("/items/" + itemDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void getItemTest() throws Exception {

        Mockito.when(itemService.get(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/" + itemDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    public void getAllItemsTest() throws Exception {

        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto);

        Mockito.when(itemService.getAll(Mockito.anyLong()))
                .thenReturn(items);

        mvc.perform(get("/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    public void searchItemsTest() throws Exception {

        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto);

        Mockito.when(itemService.search(Mockito.anyString()))
                .thenReturn(items);

        mvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .queryParam("text", itemDto.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(String.valueOf(itemDto.getId())))))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    public void crateCommentTest() throws Exception {

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setId(1L);
        comment.setText("comment");



        String json = mapper.writeValueAsString(comment);

        Mockito.when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Comment.class)))
                .thenReturn(Mapper.convertCommentToDto(comment));

        mvc.perform(post("/items/" + itemDto.getId() +"/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(comment.getId())))))
                .andExpect(jsonPath("$.authorName", is(comment.getUser().getName())))
                .andExpect(jsonPath("$.text", is(comment.getText())));
    }

    @Test
    public void createItemWithEmptyNameTest() throws Exception {

        itemDto.setName("");

        Mockito.when(itemService.add(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDto);

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

        Mockito.when(itemService.add(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDto);

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

        Mockito.when(itemService.add(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDto);

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

        Mockito.when(itemService.add(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDto);

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

        Mockito.when(itemService.add(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(itemDto);

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
}
