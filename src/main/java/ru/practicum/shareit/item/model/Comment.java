package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comments")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "text")
    String text;
    @ManyToOne
    @JoinColumn(name = "author_name")
    User user;
    @Column(name = "created")
    LocalDateTime created;
}
