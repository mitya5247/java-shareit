package ru.practicum.shareit.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class RequestControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    RequestService service;

    ObjectMapper mapper = new ObjectMapper();

    Request request;

    User user;

    @BeforeEach
    public void contextLoad() {

        user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.ru");

        request = new Request();
        request.setId(1L);
        request.setRequestor(user.getId());
        request.setDescription("description");

        mapper.registerModule(new JavaTimeModule());

    }

    @Test
    public void createRequestTest() throws Exception {


        Mockito.when(service.create(Mockito.anyLong(), Mockito.any(Request.class)))
                .thenReturn(request);

        String json = mapper.writeValueAsString(request);

        mvc.perform(post("/requests")
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor", is(Integer.parseInt(String.valueOf(request.getRequestor())))));
    }

    @Test
    public void getRequestsTest() throws Exception {

        List<Request> requestList = new ArrayList<>();

        requestList.add(request);

        Mockito.when(service.getRequestOfUser(Mockito.anyLong()))
                .thenReturn(requestList);

        mvc.perform(get("/requests")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$[0].requestor", is(Integer.parseInt(String.valueOf(request.getRequestor())))));
    }

    @Test
    public void getAllRequestsTest() throws Exception {

        List<Request> requestList = new ArrayList<>();

        requestList.add(request);

        Mockito.when(service.getAllRequest(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestList);


        mvc.perform(get("/requests/all")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$[0].requestor", is(Integer.parseInt(String.valueOf(request.getRequestor())))));
    }

    @Test
    public void getOneRequestTest() throws Exception {


        Mockito.when(service.getOneRequest(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(request);

        String json = mapper.writeValueAsString(request);

        mvc.perform(get("/requests/" + request.getId())
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(request.getId())))))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.requestor", is(Integer.parseInt(String.valueOf(request.getRequestor())))));
    }

}
