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
import ru.practicum.booking.BookingClient;
import ru.practicum.booking.BookingControllerGateway;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.User;


import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        bookingDto.setId(1L);
        bookingDto.setBookerId(user.getId());
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now());
        bookingDto.setItemId(itemDto.getId());

        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void createBookingWithNegativeIdBookerTest() throws Exception {
        BookingDto bookingDto1 = new BookingDto();
        bookingDto1.setBookerId(-1L);
        bookingDto1.setItemId(1L);

        String json = mapper.writeValueAsString(bookingDto1);

        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBookingWithNegativeIdItemTest() throws Exception {
        BookingDto bookingDto1 = new BookingDto();
        bookingDto1.setBookerId(1L);
        bookingDto1.setItemId(-1L);

        String json = mapper.writeValueAsString(bookingDto1);

        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBookingWithNullStartTest() throws Exception {

        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(null);

        String json = mapper.writeValueAsString(bookingDto);

        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBookingWithNullEndTest() throws Exception {

        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(null);

        String json = mapper.writeValueAsString(bookingDto);

        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBookingWithStartEqualsEndTest() throws Exception {

        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(bookingDto.getStart());

        String json = mapper.writeValueAsString(bookingDto);

        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBookingWithPastStartAndEndTest() throws Exception {

        bookingDto.setStart(LocalDateTime.now().minusHours(23));
        bookingDto.setEnd(LocalDateTime.now().minusHours(22));

        String json = mapper.writeValueAsString(bookingDto);


        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBookingWithPastStartEarlierThenEndTest() throws Exception {

        bookingDto.setStart(LocalDateTime.now().plusHours(2));
        bookingDto.setEnd(LocalDateTime.now().plusHours(1));

        String json = mapper.writeValueAsString(bookingDto);

        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBooking() throws Exception {

        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusHours(2));

        String json = mapper.writeValueAsString(bookingDto);

        ResponseEntity<Object> response = new ResponseEntity<>(bookingDto, HttpStatus.OK);

        Class<?> className = client.getClass();

        Method postName = className.getDeclaredMethod("post", String.class, long.class, Object.class);

        postName.setAccessible(true);

        Mockito.when(postName.invoke(client, Mockito.anyString(), Mockito.anyLong(), Mockito.any(BookingDto.class)))
                        .thenReturn(response);

        mvc.perform(post("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(bookingDto.getId())))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.bookerId", is(Integer.parseInt(String.valueOf(bookingDto.getBookerId())))))
                .andExpect(jsonPath("$.itemId", is(Integer.parseInt(String.valueOf(bookingDto.getItemId())))));
    }

    @Test
    public void getBookingWrongStateTest() throws Exception {


        mvc.perform(get("/bookings?state=NO_NORMAL_STATE")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getBookingByOwnerWrongStateTest() throws Exception {


        mvc.perform(get("/bookings/owner?state=NO_NORMAL_STATE")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
