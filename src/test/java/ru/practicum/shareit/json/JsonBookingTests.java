package ru.practicum.shareit.json;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonBookingTests {

    @Autowired
    JacksonTester<BookingDtoResponse> tester;

    User user;

    ItemDto itemDto;

    BookingDtoResponse bookingDtoResponse;

    @BeforeEach
    public void loadContext() {
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
        itemDto.setRequestId(1L);
        itemDto.setLastBooking(new BookingDto());
        itemDto.setNextBooking(new BookingDto());

        bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(1L);
        bookingDtoResponse.setBooker(user);
        bookingDtoResponse.setItem(itemDto);
    }

    @Test
    public void serializeBookingDtoResponse() throws IOException {
        JsonContent<BookingDtoResponse> content = tester.write(bookingDtoResponse);

        assertThat(content).extractingJsonPathNumberValue("$.id", bookingDtoResponse.getId());
        assertThat(content).extractingJsonPathValue("$.booker", bookingDtoResponse.getBooker());
        assertThat(content).extractingJsonPathValue("$.item", bookingDtoResponse.getItem());
        assertThat(content).extractingJsonPathValue("$.status", bookingDtoResponse.getStatus());
        assertThat(content).extractingJsonPathValue("$.start", bookingDtoResponse.getStart());
        assertThat(content).extractingJsonPathValue("$.end", bookingDtoResponse.getEnd());

    }


}
