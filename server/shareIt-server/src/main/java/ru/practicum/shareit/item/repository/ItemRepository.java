package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    public List<Item> findAllByOwnerOrderById(Long userId);

    public List<Item> findAllByNameOrDescriptionContainingIgnoreCase(String word, String word1);

}
