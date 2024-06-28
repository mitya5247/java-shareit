package ru.practicum.shareit.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

@SpringBootTest(properties = "db.name = test")
public class ServiceItemTests {
    @MockBean
    ItemRepository itemRepository;
    @Autowired
    ItemService service;
    @MockBean
    UserRepository userRepository;
    ItemDto itemDto;
    Item item;
    User user;

    @BeforeEach
    public void createUserAndItem() {
        user = new User();
        user.setId(1L);
        user.setEmail("email@name.ru");
        user.setName("name");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setAvailable(true);
        itemDto.setName("название");
        itemDto.setDescription("описание");

        item = Mapper.convertToItem(user.getId(), itemDto);
    }

    @Test
    public void createItemTest() {

        Mockito.when(userRepository.findById(user.getId()))
                        .thenReturn(Optional.ofNullable(user));

        Mockito.when(itemRepository.save(Mapper.convertToItem(user.getId(), itemDto)))
                .thenReturn(item);

        Mockito.when(itemRepository.save(item))
                .thenReturn(item);


        Mockito.verify(itemRepository, Mockito.times(1))
                .save(Mapper.convertToItem(user.getId(), itemDto));


        Assertions.assertEquals(service.add(user.getId(), itemDto), itemDto);

    }
}
