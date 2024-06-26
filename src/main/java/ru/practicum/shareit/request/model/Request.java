package ru.practicum.shareit.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotEmpty(message = "description musnt't be empty")
    @Column(name = "description")
    String description;
    @Column(name = "requestor_id")
    Long requestor;
    @Transient
    List<ItemRequestDto> items;
    @Column(name = "created")
    LocalDateTime created = LocalDateTime.now();
}
