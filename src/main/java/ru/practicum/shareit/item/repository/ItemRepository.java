package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
  //  public ItemDto add(Long userId, ItemDto itemDto);

  //  public ItemDto update(Long userId, Long itemId, ItemDto itemDto);

//    public ItemDto get(Long id);

 //   public List<ItemDto> getAll(Long userId);

 //   public List<ItemDto> search(String word);

   // public ItemDto save(Long userId, ItemDto itemDto);

  //  public ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    public Optional<Item> findById(Long id);

    public List<Item> findAllByOwner(Long userId);

    public List<Item> findAllByNameOrDescriptionIgnoreCase(String word, String sameWord);

 //   public List<ItemDto> getAll();

}
