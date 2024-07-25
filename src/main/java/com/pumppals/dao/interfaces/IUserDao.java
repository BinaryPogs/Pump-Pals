package com.pumppals.dao.interfaces;
import com.pumppals.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface IUserDao {
    User createUser(User user) throws SQLException;
    Optional<User> getUserById(UUID id) throws SQLException;
    List<User> getAllUsers();
    void updateUser(User user);
    boolean deleteUser(UUID id);
}
