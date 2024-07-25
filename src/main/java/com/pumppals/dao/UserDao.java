package com.pumppals.dao;

import com.pumppals.dao.interfaces.IUserDao;
import com.pumppals.database.DatabaseManager;
import com.pumppals.database.TransactionManager;
import com.pumppals.models.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public class UserDao implements IUserDao {

    private static final Logger logger = LoggerFactory.getLogger(User.class);
    @Override
    public User createUser(User user) throws SQLException {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                session.persist(user);
                return user;
            });
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create user", e);
        }
    }
    @Override
    public Optional<User> getUserById(UUID id) throws SQLException {
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                User user = session.get(User.class, id);
                if (user == null) {
                    logger.warn("No challenge found with ID: {}", id);
                }
                return Optional.ofNullable(user);
            });
        } catch (Exception e) {
            logger.error("Error retrieving user with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve user", e);
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
            throw new RuntimeException("Failed to retrieve all users", e);
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
            throw new RuntimeException("Failed to update user", e);
        }
    }
    @Override
    public boolean deleteUser(UUID id){
        try {
            return TransactionManager.executeInTransaction(() -> {
                Session session = DatabaseManager.getSessionFactory().getCurrentSession();
                User user = session.get(User.class, id);
                if (user != null) {
                    session.remove(user);
                    logger.info("Deleted user with ID: {}", id);
                    return true;
                } else {
                    logger.warn("Attempted to delete non-existent user wiht ID: {}", id);
                    return false;
                }
            });
        } catch (Exception e) {
            logger.error("Error deleting challenge with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
