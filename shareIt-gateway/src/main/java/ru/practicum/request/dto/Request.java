package ru.practicum.request.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class Request {
    Long id;
    @NotBlank(message = "description musnt't be empty and null")
    String description;
    @Positive(message = "requestor musn't be negative")
    Long requestor;
    List<ItemRequestDto> items;
    LocalDateTime created = LocalDateTime.now();
}
