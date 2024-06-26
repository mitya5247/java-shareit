package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequestorOrderByCreatedDesc(Long requestorId);

   // @Query(value = "select new ItemRequestDto as ird from Item as i where i.requestId = :requestId ")
  //  List<ItemRequestDto> findItems(Long requestId);
}
