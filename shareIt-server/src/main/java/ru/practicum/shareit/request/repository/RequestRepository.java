package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequestorOrderByCreatedDesc(Long requestorId);

    @Query(value = "select new ru.practicum.shareit.request.dto.ItemRequestDto(i.id, i.name, i.description, " +
            "i.request.id, i.available) from Item as i where i.request.id = :requestId order by i.request.created desc")
    List<ItemRequestDto> findItems(Long requestId);

}
