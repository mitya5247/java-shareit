package ru.practicum.shareit_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.Constants;
import ru.practicum.item.ItemClient;
import ru.practicum.item.ItemControllerGateway;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
