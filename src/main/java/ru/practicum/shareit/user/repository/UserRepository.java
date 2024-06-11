package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exceptions.EmailAlreadyExistsException;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
 //   public User create(User user) throws EmailAlreadyExistsException;

  //  public User update(Long userId, User user);

 //   public void delete(Long id);

  //  public User get(Long id);

  //  public List<User> getAll();
}
