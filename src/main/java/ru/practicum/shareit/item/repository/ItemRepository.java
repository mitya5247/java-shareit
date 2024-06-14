package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    public Optional<Item> findById(Long id);

    public List<Item> findAllByOwnerOrderById(Long userId);

 //   public List<ItemDto> findByName(String name);

    public List<Item> findAllByNameOrDescriptionContainingIgnoreCase(String word, String word1);

}
