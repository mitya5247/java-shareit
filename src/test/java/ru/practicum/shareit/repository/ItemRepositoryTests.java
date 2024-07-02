package ru.practicum.shareit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

@DataJpaTest(properties = "db.name = test")
public class ItemRepositoryTests {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    User user;
    Item item;
    ItemDto itemDto;

    Long userId;
    Long itemId;

    @BeforeEach
    public void initUser() {
        user = new User();
        user.setEmail("email@name.ru");
        user.setName("name");

        userId = userRepository.save(user).getId();
        user.setId(userId);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setAvailable(true);
        itemDto.setName("название");
        itemDto.setDescription("описание");

        itemDto.setComments(new ArrayList<>());

        item = Mapper.convertToItem(user.getId(), itemDto);
        item.setComments(new ArrayList<>());
        itemId = itemRepository.save(item).getId();
    }

    @Test
    public void create() {
        TypedQuery<Item> query = em.createQuery("select i from Item i where i.id = :id", Item.class)
                        .setParameter("id", itemId);
        Item item1 = query.getSingleResult();
        Assertions.assertEquals(itemId , item1.getId());
        Assertions.assertEquals(itemDto.getName() , item1.getName());
        Assertions.assertEquals(itemDto.getDescription() , item1.getDescription());
    }

    @Test
    public void update() {
        item.setName("Новое имя");
        itemId = itemRepository.save(item).getId();
        TypedQuery<Item> query = em.createQuery("select i from Item i where i.id = :id", Item.class)
                .setParameter("id", itemId);
        Item item1 = query.getSingleResult();
        Assertions.assertEquals(itemId , item1.getId());
        Assertions.assertEquals(item.getName() , item1.getName());
        Assertions.assertEquals(item.getDescription() , item1.getDescription());
    }

    @Test
    public void get() {
        itemRepository.save(item);
        TypedQuery<Item> query = em.createQuery("select i from Item i where i.id = :id", Item.class)
                .setParameter("id", itemId);
        Item item1 = query.getSingleResult();
        Assertions.assertEquals(itemId , item1.getId());
        Assertions.assertEquals(item.getName() , item1.getName());
        Assertions.assertEquals(item.getDescription() , item1.getDescription());
    }

    @Test
    public void addComment() {

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setText("коммент");

        Long commentId = commentRepository.save(comment).getId();
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.id = :id", Comment.class)
                .setParameter("id", commentId);
        Comment comment1 = query.getSingleResult();
        Assertions.assertEquals(commentId , comment1.getId());
        Assertions.assertEquals(comment.getText() , comment1.getText());
        Assertions.assertEquals(comment.getUser() , comment1.getUser());
    }

    @Test
    public void findByIdCommentTest() {

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setText("коммент");

        Long commentId = commentRepository.save(comment).getId();
        Comment comment1 = commentRepository.findById(commentId).get();
        Assertions.assertEquals(commentId , comment1.getId());
        Assertions.assertEquals(comment.getText() , comment1.getText());
        Assertions.assertEquals(comment.getUser() , comment1.getUser());
    }

    @AfterEach
    public void delete() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

}
