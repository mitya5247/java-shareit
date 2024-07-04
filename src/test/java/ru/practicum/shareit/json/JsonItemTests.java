package ru.practicum.shareit.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonItemTests {

    @Autowired
    JacksonTester<ItemDto> tester;

    User user;

    ItemDto itemDto;

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
    }

    @Test
    public void serializeItemTest() throws IOException {

        JsonContent<ItemDto> content = tester.write(itemDto);

        assertThat(content).extractingJsonPathNumberValue("$.id", itemDto.getId());
        assertThat(content).extractingJsonPathStringValue("$.name", itemDto.getName());
        assertThat(content).extractingJsonPathBooleanValue("$.available", itemDto.getAvailable());
        assertThat(content).extractingJsonPathStringValue("$.description", itemDto.getDescription());
        assertThat(content).extractingJsonPathValue("$.lastBooking", itemDto.getLastBooking());
        assertThat(content).extractingJsonPathValue("$.nextBooking", itemDto.getNextBooking());
        assertThat(content).extractingJsonPathArrayValue("$.comments", itemDto.getComments());
        assertThat(content).extractingJsonPathNumberValue("$.requestId", itemDto.getRequestId());

    }
    @Test
    public void deSerializeItemTest() throws IOException {
        JsonContent<ItemDto> content = tester.write(itemDto);

        String json = content.getJson();

        ObjectContent<ItemDto> objectContent = tester.parse(json);

        ItemDto itemDto1 = objectContent.getObject();

        objectContent.assertThat();

        Assertions.assertEquals(itemDto.getId(), itemDto1.getId());
        Assertions.assertEquals(itemDto.getName(), itemDto1.getName());
        Assertions.assertEquals(itemDto.getDescription(), itemDto1.getDescription());
        Assertions.assertEquals(itemDto.getAvailable(), itemDto1.getAvailable());
        Assertions.assertEquals(itemDto.getComments(), itemDto1.getComments());
        Assertions.assertEquals(itemDto.getRequestId(), itemDto1.getRequestId());
        Assertions.assertEquals(itemDto.getLastBooking(), itemDto1.getLastBooking());
        Assertions.assertEquals(itemDto.getNextBooking(), itemDto1.getNextBooking());

    }

}
