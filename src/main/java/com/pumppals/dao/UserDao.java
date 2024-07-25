package com.pumppals.dao;

import com.pumppals.dao.interfaces.IUserDao;
import com.pumppals.database.DatabaseManager;
import com.pumppals.database.TransactionManager;
import com.pumppals.models.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserDao implements IUserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public User createUser(User user) {
        // Check if the user already has an ID
        if (user.getId() != null) {
            logger.error("Attempted to create a user but ID was already set: {}", user.getId());
            throw new IllegalStateException("User ID should not be set for new user creation");
        }
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                session.persist(user);
                session.flush();  // Ensure ID is assigned and entity is persisted
                return user;
            });
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to create user", e);
        }
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                User user = session.get(User.class, id);
                if (user == null) {
                    logger.warn("No user found with ID: {}", id);
                }
                return Optional.ofNullable(user);
            });
        } catch (Exception e) {
            logger.error("Error retrieving user with ID {}: {}", id, e.getMessage(), e);
            throw new IllegalStateException("Failed to retrieve user", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                return session.createQuery("FROM User", User.class).list();
            });
        } catch (Exception e) {
            logger.error("Error retrieving all users: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to retrieve all users", e);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                session.merge(user);
                logger.info("Updated user with ID: {}", user.getId());
            });
        } catch (Exception e) {
            logger.error("Error updating user with ID {}: {}", user.getId(), e.getMessage(), e);
            throw new IllegalStateException("Failed to update user", e);
        }
    }

    @Override
    public boolean deleteUser(UUID id) {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
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
            throw new IllegalStateException("Failed to delete user", e);
        }
    }
}
