package com.pumppals.dao;

import com.pumppals.dao.exceptions.DatabaseException;
import com.pumppals.dao.exceptions.UserAlreadyExistsException;
import com.pumppals.dao.exceptions.UserNotFoundException;
import com.pumppals.dao.interfaces.IUserDao;
import com.pumppals.database.TransactionManager;
import com.pumppals.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDao implements IUserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Override
    public User createUser(User user) {
        if (user.getId() != null) {
            logger.error("Attempted to create a user but ID was already set: {}", user.getId());
            throw new IllegalArgumentException("User ID should not be set for new user creation");
        }
        try {
            return TransactionManager.executeInTransaction(session -> {
                if (isUsernameExists(user.getUsername())) {
                    throw new UserAlreadyExistsException(user.getUsername());
                }
                session.persist(user);
                session.flush();
                return user;
            });
        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            throw new DatabaseException("Failed to create user", e);
        }
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        try {
            return TransactionManager.executeInTransaction(session ->
                    Optional.ofNullable(session.get(User.class, id))
            );
        } catch (Exception e) {
            logger.error("Error retrieving user with ID {}: {}", id, e.getMessage(), e);
            throw new DatabaseException("Failed to retrieve user", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return TransactionManager.executeInTransaction(session ->
                    session.createQuery("FROM User", User.class).list()
            );
        } catch (Exception e) {
            logger.error("Error retrieving all users: {}", e.getMessage(), e);
            throw new DatabaseException("Failed to retrieve all users", e);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            TransactionManager.executeInTransaction(session -> {
                User existingUser = session.get(User.class, user.getId());
                if (existingUser == null) {
                    throw new UserNotFoundException(user.getId());
                }
                if (!existingUser.getUsername().equals(user.getUsername()) && isUsernameExists(user.getUsername())) {
                    throw new UserAlreadyExistsException(user.getUsername());
                }
                session.merge(user);
                logger.info("Updated user with ID: {}", user.getId());
                return null;
            });
        } catch (UserNotFoundException | UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error updating user with ID {}: {}", user.getId(), e.getMessage(), e);
            throw new DatabaseException("Failed to update user", e);
        }
    }

    @Override
    public boolean deleteUser(UUID id) {
        try {
            return TransactionManager.executeInTransaction(session -> {
                User user = session.get(User.class, id);
                if (user != null) {
                    session.remove(user);
                    logger.info("Deleted user with ID: {}", id);
                    return true;
                } else {
                    logger.warn("Attempted to delete non-existent user with ID: {}", id);
                    return false;
                }
            });
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: {}", id, e.getMessage(), e);
            throw new DatabaseException("Failed to delete user", e);
        }
    }

    private boolean isUsernameExists(String username) {
        return TransactionManager.executeInTransaction(session -> {
            Long count = session.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        });
    }
}