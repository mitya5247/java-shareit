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
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTests {
    @Autowired
    MockMvc mvc;

    @MockBean
    BookingService service;

    User user;

    BookingDtoResponse bookingDto;

    ItemDto itemDto;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void contextLoad() {

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

        bookingDto = new BookingDtoResponse();
        bookingDto.setId(1L);
        bookingDto.setBooker(user);
        bookingDto.setItem(itemDto);

        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void createBookingTest() throws Exception {
        Mockito.when(service.createRequest(Mockito.anyLong(), Mockito.any(BookingDto.class)))
                .thenReturn(bookingDto);

        String json = mapper.writeValueAsString(bookingDto);

        mvc.perform(post("/bookings")
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(bookingDto.getId())))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(Integer.parseInt(String.valueOf(bookingDto.getBooker().getId())))))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))

                .andExpect(jsonPath("$.item.id", is(Integer.parseInt(String.valueOf(bookingDto.getItem().getId())))))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())));
    }

    @Test
    public void updateBookingTest() throws Exception {
        Mockito.when(service.updateState(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(bookingDto);

        String json = mapper.writeValueAsString(bookingDto);

        mvc.perform(patch("/bookings/" + bookingDto.getId())
                        .content(json)
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(bookingDto.getId())))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(Integer.parseInt(String.valueOf(bookingDto.getBooker().getId())))))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))

                .andExpect(jsonPath("$.item.id", is(Integer.parseInt(String.valueOf(bookingDto.getItem().getId())))))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())));
    }

    @Test
    public void getBookingTest() throws Exception {
        Mockito.when(service.get(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/" + bookingDto.getId())
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(bookingDto.getId())))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())))
                .andExpect(jsonPath("$.booker.id", is(Integer.parseInt(String.valueOf(bookingDto.getBooker().getId())))))
                .andExpect(jsonPath("$.booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName())))

                .andExpect(jsonPath("$.item.id", is(Integer.parseInt(String.valueOf(bookingDto.getItem().getId())))))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$.item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$.item.available", is(bookingDto.getItem().getAvailable())));
    }

    @Test
    public void getAllUserBookingsTest() throws Exception {
        bookingDto.setStatus(State.WAITING);
        List<BookingDtoResponse> responseList = new ArrayList<>();
        responseList.add(bookingDto);

        Mockito.when(service.getAllUserBookings(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(responseList);

        mvc.perform(get("/bookings")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("from", String.valueOf(0))
                        .queryParam("size", String.valueOf(10))
                        .queryParam("state", String.valueOf(State.WAITING))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(String.valueOf(bookingDto.getId())))))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingDto.getStatus()))))
                .andExpect(jsonPath("$[0].booker.id", is(Integer.parseInt(String.valueOf(bookingDto.getBooker().getId())))))
                .andExpect(jsonPath("$[0].booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))

                .andExpect(jsonPath("$[0].item.id", is(Integer.parseInt(String.valueOf(bookingDto.getItem().getId())))))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(bookingDto.getItem().getAvailable())));
    }

    @Test
    public void getAllItemsBookedTest() throws Exception {
        bookingDto.setStatus(State.WAITING);
        List<BookingDtoResponse> responseList = new ArrayList<>();
        responseList.add(bookingDto);

        Mockito.when(service.getAllItemsBooked(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(responseList);

        mvc.perform(get("/bookings/owner")
                        .header(Constants.HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .queryParam("from", String.valueOf(0))
                        .queryParam("size", String.valueOf(10))
                        .queryParam("state", String.valueOf(State.WAITING))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(Integer.parseInt(String.valueOf(bookingDto.getId())))))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(bookingDto.getStatus()))))
                .andExpect(jsonPath("$[0].booker.id", is(Integer.parseInt(String.valueOf(bookingDto.getBooker().getId())))))
                .andExpect(jsonPath("$[0].booker.email", is(bookingDto.getBooker().getEmail())))
                .andExpect(jsonPath("$[0].booker.name", is(bookingDto.getBooker().getName())))

                .andExpect(jsonPath("$[0].item.id", is(Integer.parseInt(String.valueOf(bookingDto.getItem().getId())))))
                .andExpect(jsonPath("$[0].item.name", is(bookingDto.getItem().getName())))
                .andExpect(jsonPath("$[0].item.description", is(bookingDto.getItem().getDescription())))
                .andExpect(jsonPath("$[0].item.available", is(bookingDto.getItem().getAvailable())));
    }
}
