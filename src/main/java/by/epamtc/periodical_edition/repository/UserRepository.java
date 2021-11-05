package by.epamtc.periodical_edition.repository;
import by.epamtc.periodical_edition.entity.User;

import java.util.List;

public interface UserRepository {
    User findById(Long roleId);
    List<User> findAll();
    boolean add(User user);
    boolean update(User user);
    boolean delete(Long userId);
}
