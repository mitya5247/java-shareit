package ru.practicum.shareit_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.Constants;
import ru.practicum.booking.BookingClient;
import ru.practicum.booking.BookingControllerGateway;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingControllerGateway.class)
public class BookingControllerGatewayTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    BookingClient client;

    User user;
    ItemDto itemDto;
    BookingDto bookingDto;

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

        bookingDto = new BookingDto();
        bookingDto.setBookerId(user.getId());
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusHours(23));
        bookingDto.setItemId(itemDto.getId());

        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void sendRequest() throws Exception {

        String json = mapper.writeValueAsString(bookingDto);


        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header(Constants.HEADER, user.getId())
                        .content(json))
                .andExpect(status().isOk());
    }
}
