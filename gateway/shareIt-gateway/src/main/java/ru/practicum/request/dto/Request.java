package ru.practicum.request.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class Request {
    Long id;
    @NotBlank(message = "description musnt't be empty")
    @NotNull(message = "description musnt't be null")
    String description;
    @Positive(message = "requestor musn't be negative")
    Long requestor;
    List<ItemRequestDto> items;
    @Column(name = "created")
    LocalDateTime created = LocalDateTime.now();
}
